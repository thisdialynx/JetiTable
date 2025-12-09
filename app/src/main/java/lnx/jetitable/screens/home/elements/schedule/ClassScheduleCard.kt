package lnx.jetitable.screens.home.elements.schedule

import android.icu.util.Calendar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.api.timetable.data.query.AttendanceListData
import lnx.jetitable.misc.DataState
import lnx.jetitable.misc.DateManager
import lnx.jetitable.screens.home.data.ClassUiData
import lnx.jetitable.screens.home.elements.SiteButton
import lnx.jetitable.screens.home.elements.datepicker.DatePickerView
import lnx.jetitable.screens.home.elements.datepicker.DateState
import lnx.jetitable.ui.components.StateStatus
import lnx.jetitable.ui.icons.Moodle
import lnx.jetitable.ui.icons.google.CalendarMonth
import lnx.jetitable.ui.icons.google.Info
import lnx.jetitable.ui.icons.google.Mood

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassScheduleCard(
    classList: DataState<out List<ClassUiData>>,
    attendanceList: DataState<out List<AttendanceListData>>,
    studentFullName: String,
    dateState: DateState,
    connectionState: DataState<out Boolean>,
    onAttendanceListRequest: (ClassUiData) -> Unit,
    onPresenceVerify: (ClassUiData) -> Unit,
    onDateUpdate: (Calendar) -> Unit,
    onBackwardDateShift: () -> Unit,
    onForwardDateShift: () -> Unit
) {
    val localUriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboard.current

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardTitle(dateState, connectionState, onDateUpdate, onForwardDateShift, onBackwardDateShift)
            Content(localUriHandler, clipboardManager, classList, attendanceList, studentFullName, onAttendanceListRequest, onPresenceVerify)
        }
    }
}

@Composable
private fun CardTitle(
    dateState: DateState,
    connectionState: DataState<out Boolean>,
    onDateUpdate: (Calendar) -> Unit,
    onForwardDateShift: () -> Unit,
    onBackwardDateShift: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = CalendarMonth,
            contentDescription = null,
            modifier = Modifier.padding(start = 4.dp)
        )
        Row(modifier = Modifier.weight(1f)) {
            DatePickerView(
                datePickerState = dateState.datePickerState,
                formattedDate = dateState.formattedDate,
                onDateSelected = onDateUpdate,
                connectionState = connectionState
            )
        }
        IconButton(
            onClick = onBackwardDateShift,
            modifier = Modifier.padding(end = 4.dp),
            enabled = connectionState == DataState.Success(true)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                contentDescription = null
            )
        }
        IconButton(
            onClick = onForwardDateShift,
            enabled = connectionState == DataState.Success(true)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun Content(
    localUriHandler: UriHandler,
    clipboardManager: Clipboard,
    classList: DataState<out List<ClassUiData>>,
    attendanceList: DataState<out List<AttendanceListData>>,
    studentFullName: String,
    onAttendanceListRequest: (ClassUiData) -> Unit,
    onPresenceVerify: (ClassUiData) -> Unit
) {
    val targetColor = if (classList is DataState.Success)
        MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp) else
        MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = targetColor
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        AnimatedContent(
            targetState = classList
        ) { classList ->
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (classList) {
                    is DataState.Empty -> {
                        StateStatus(
                            icon = Mood,
                            description = stringResource(R.string.no_classes)
                        )
                    }
                    is DataState.Loading -> {
                        StateStatus(
                            description = stringResource(R.string.getting_list)
                        )
                    }
                    is DataState.Success -> {
                        classList.data.forEachIndexed { index, classItem ->
                            val cardColors = if (classItem.isNow) {
                                MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp) to MaterialTheme.colorScheme.onSurface
                            }

                            ContentRow(
                                index = index,
                                isLastElement = index == classList.data.size - 1,
                                contentColors = cardColors,
                                classUiData = classItem,
                                attendanceList = attendanceList,
                                studentFullName = studentFullName,
                                onAttendanceListRequest = onAttendanceListRequest,
                                localUriHandler = localUriHandler,
                                clipboardManager = clipboardManager,
                                onMeetingUrlClick = { onPresenceVerify(classItem) },
                            )
                        }
                    }
                    is DataState.Error -> {
                        StateStatus(
                            icon = Info,
                            description = stringResource(classList.messageResId)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ContentRow(
    index: Int,
    isLastElement: Boolean,
    contentColors: Pair<Color, Color>,
    classUiData: ClassUiData,
    attendanceList: DataState<out List<AttendanceListData>>,
    studentFullName: String,
    localUriHandler: UriHandler,
    clipboardManager: Clipboard,
    onAttendanceListRequest: (ClassUiData) -> Unit,
    onMeetingUrlClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = getCardShape(index, isLastElement)

    SharedTransitionLayout {
        AnimatedContent(
            targetState = expanded,
            label = "row_transition"
        ) { targetState ->
            if (!targetState) {
                MainContent(
                    data = classUiData,
                    cardColors = contentColors,
                    shape = shape,
                    localUriHandler = localUriHandler,
                    clipboardManager = clipboardManager,
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    onClick = { expanded = true },
                    onMeetingUrlClick = onMeetingUrlClick
                )
            } else {
                DetailsContent(
                    data = classUiData,
                    attendanceList = attendanceList,
                    studentFullName = studentFullName,
                    onAttendanceListRequest = onAttendanceListRequest,
                    cardColors = contentColors,
                    shape = shape,
                    localUriHandler = localUriHandler,
                    clipboardManager = clipboardManager,
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    onClick = { expanded = false },
                    onMeetingUrlClick = onMeetingUrlClick
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MainContent(
    data: ClassUiData,
    cardColors: Pair<Color, Color>,
    shape: RoundedCornerShape,
    localUriHandler: UriHandler,
    clipboardManager: Clipboard,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    onClick: () -> Unit,
    onMeetingUrlClick: () -> Unit,
) {
    with(sharedTransitionScope) {
        Surface(
            color = cardColors.first,
            onClick = onClick,
            shape = shape,
            modifier = Modifier.sharedElement(
                rememberSharedContentState("surface"),
                animatedVisibilityScope = animatedVisibilityScope,
            )
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "${data.start}\n${data.end}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.sharedBounds(
                            rememberSharedContentState("time"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                        ),
                        color = cardColors.second
                    )
                    Text(
                        text = data.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .weight(1f)
                            .sharedBounds(
                                rememberSharedContentState("title"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                            ),
                        color = cardColors.second
                    )
                    Text(
                        text = data.type,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.sharedBounds(
                            rememberSharedContentState("type"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                        ),
                        color = cardColors.second
                    )

                    if (data.meetingLink.isNotEmpty()) {
                        SiteButton(
                            url = data.meetingLink,
                            icon = getMeetingIcon(data.meetingLink),
                            color = MaterialTheme.colorScheme.secondary,
                            uriHandler = localUriHandler,
                            clipboardManager = clipboardManager,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState("meeting"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                            onButtonClick = onMeetingUrlClick
                        )
                    }

                    if (data.moodleLink.isNotEmpty()) {
                        SiteButton(
                            url = data.moodleLink,
                            icon = Moodle,
                            color = MaterialTheme.colorScheme.tertiary,
                            uriHandler = localUriHandler,
                            clipboardManager = clipboardManager,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState("moodle"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailsContent(
    data: ClassUiData,
    attendanceList: DataState<out List<AttendanceListData>>,
    studentFullName: String,
    cardColors: Pair<Color, Color>,
    shape: RoundedCornerShape,
    localUriHandler: UriHandler,
    clipboardManager: Clipboard,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    onAttendanceListRequest: (ClassUiData) -> Unit,
    onClick: () -> Unit,
    onMeetingUrlClick: () -> Unit,
) {
    var isAttendanceListOpen by remember { mutableStateOf(false) }

    AttendanceDialog(
        isOpen = isAttendanceListOpen,
        list = attendanceList,
        studentFullName = studentFullName,
        onDismissRequest = {
            isAttendanceListOpen = false
        }
    )

    with(sharedTransitionScope) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .sharedElement(
                    rememberSharedContentState("surface"),
                    animatedVisibilityScope = animatedVisibilityScope
                ),
            shape = shape,
            color = cardColors.first,
            onClick = onClick
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = data.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .weight(1f)
                            .sharedBounds(
                                rememberSharedContentState("title"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                            ),
                        color = cardColors.second
                    )
                    Text(
                        text = data.type,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.sharedBounds(
                            rememberSharedContentState("type"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                        ),
                        color = cardColors.second
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${data.start}-${data.end} (${data.number})",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.sharedBounds(
                        rememberSharedContentState("time"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                    ),
                    color = cardColors.second
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    data.weeks.forEach { week ->
                        val dateManager = DateManager()
                        val currentSemesterWeek = dateManager.getCurrentSemesterWeek()
                        val isCurrentWeek = week.toInt() == currentSemesterWeek

                        Surface(
                            shape = CircleShape,
                            color = if (isCurrentWeek) MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp) else
                                MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Text(
                                    text = week,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (isCurrentWeek) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "${data.educator}, ${data.group}.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .size(40.dp),
                        onClick = {
                            onAttendanceListRequest(data)
                            isAttendanceListOpen = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.List,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    if (data.meetingLink.isNotEmpty()) {
                        SiteButton(
                            url = data.meetingLink,
                            icon = getMeetingIcon(data.meetingLink),
                            color = MaterialTheme.colorScheme.secondary,
                            uriHandler = localUriHandler,
                            clipboardManager = clipboardManager,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState("meeting"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                            onButtonClick = onMeetingUrlClick
                        )
                    }

                    if (data.moodleLink.isNotEmpty()) {
                        SiteButton(
                            url = data.moodleLink,
                            icon = Moodle,
                            color = MaterialTheme.colorScheme.tertiary,
                            uriHandler = localUriHandler,
                            clipboardManager = clipboardManager,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState("moodle"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        )
                    }
                }
            }
        }
    }
}