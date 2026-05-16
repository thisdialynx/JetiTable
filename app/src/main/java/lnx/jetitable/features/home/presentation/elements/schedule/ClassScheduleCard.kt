package lnx.jetitable.features.home.presentation.elements.schedule

import android.icu.util.Calendar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.features.home.domain.models.AttendanceResult
import lnx.jetitable.features.home.domain.models.ScheduleFetchFailureReason
import lnx.jetitable.features.home.domain.models.ScheduleResult
import lnx.jetitable.features.home.presentation.ClassUiData
import lnx.jetitable.features.home.presentation.elements.datepicker.DatePickerView
import lnx.jetitable.features.home.presentation.elements.datepicker.DateState
import lnx.jetitable.misc.DateHelper
import lnx.jetitable.ui.components.SiteButton
import lnx.jetitable.ui.components.StateStatus
import lnx.jetitable.ui.components.getColumnCardShape
import lnx.jetitable.ui.icons.google.CalendarMonth
import lnx.jetitable.ui.icons.google.Circle
import lnx.jetitable.ui.icons.google.Error
import lnx.jetitable.ui.icons.google.Info
import lnx.jetitable.ui.icons.google.Mood

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassScheduleCard(
    classList: ScheduleResult<List<ClassUiData>>,
    attendanceList: AttendanceResult,
    studentFullName: String,
    dateState: DateState,
    onAttendanceListRequest: (ClassUiData) -> Unit,
    onPresenceVerify: (ClassUiData) -> Unit,
    onDateUpdate: (Calendar) -> Unit,
    onBackwardDateShift: () -> Unit,
    onForwardDateShift: () -> Unit
) {
    val localUriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboard.current
    val enabled =
        !(classList is ScheduleResult.Failure && classList.error == ScheduleFetchFailureReason.NO_CACHE)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardTitle(dateState, enabled, onDateUpdate, onForwardDateShift, onBackwardDateShift)

            Content(
                localUriHandler = localUriHandler,
                clipboardManager = clipboardManager,
                classList = classList,
                attendanceList = attendanceList,
                studentFullName = studentFullName,
                onAttendanceListRequest = onAttendanceListRequest,
                onPresenceVerify = onPresenceVerify
            )
        }
    }
}

@Composable
private fun CardTitle(
    dateState: DateState,
    enabled: Boolean,
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
                enabled = enabled,
                formattedDate = dateState.formattedDate,
                onDateSelected = onDateUpdate,
            )
        }
        IconButton(
            onClick = onBackwardDateShift,
            modifier = Modifier.padding(end = 4.dp),
            enabled = enabled
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                contentDescription = null
            )
        }
        IconButton(
            onClick = onForwardDateShift,
            enabled = enabled
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
    classList: ScheduleResult<List<ClassUiData>>,
    attendanceList: AttendanceResult,
    studentFullName: String,
    onAttendanceListRequest: (ClassUiData) -> Unit,
    onPresenceVerify: (ClassUiData) -> Unit
) {
    val targetColor = when (classList) {
        is ScheduleResult.Success -> {
            if (classList.data.isEmpty()) MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp) else {
                MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
            }
        }
        else ->  MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = targetColor
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        AnimatedContent(
            targetState = classList
        ) { list ->
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (list) {
                    is ScheduleResult.Success -> {
                        list.data.forEachIndexed { index, classItem ->
                            val cardColors = if (classItem.isNow) {
                                MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp) to MaterialTheme.colorScheme.onSurface
                            }

                            ContentRow(
                                index = index,
                                isLastElement = index == list.data.size - 1,
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

                    is ScheduleResult.Loading -> {
                        StateStatus(
                            description = stringResource(R.string.loading)
                        )
                    }

                    is ScheduleResult.Failure -> {
                        val reason = when (list.error) {
                            ScheduleFetchFailureReason.EMPTY -> R.string.no_schedule_available_for_today to Mood
                            ScheduleFetchFailureReason.UNKNOWN_ERROR -> R.string.unknown_error to Error
                            ScheduleFetchFailureReason.NETWORK_ERROR -> R.string.network_error to Error
                            ScheduleFetchFailureReason.IO_ERROR -> R.string.something_went_wrong to Info
                            ScheduleFetchFailureReason.NO_CACHE -> R.string.no_cached_schedule to Circle
                        }

                        StateStatus(
                            icon = reason.second,
                            description = stringResource(reason.first)
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
    attendanceList: AttendanceResult,
    studentFullName: String,
    localUriHandler: UriHandler,
    clipboardManager: Clipboard,
    onAttendanceListRequest: (ClassUiData) -> Unit,
    onMeetingUrlClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = getColumnCardShape(index, isLastElement)

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
                        color = cardColors.second,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
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
    attendanceList: AttendanceResult,
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
    val haptic = LocalHapticFeedback.current

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
                        style = MaterialTheme.typography.bodyLarge,
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
                        val dateHelper = DateHelper()
                        val currentSemesterWeek = dateHelper.getCurrentSemesterWeek()
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
                            .size(40.dp)
                            .combinedClickable(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                                    onAttendanceListRequest(data)
                                    isAttendanceListOpen = true
                                },
                                onLongClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onMeetingUrlClick()
                                }
                            )
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