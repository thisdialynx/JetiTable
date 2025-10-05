package lnx.jetitable.screens.home.elements.schedule

import android.icu.util.Calendar
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.misc.DataState
import lnx.jetitable.screens.home.data.ClassUiData
import lnx.jetitable.screens.home.elements.datepicker.DatePickerView
import lnx.jetitable.screens.home.elements.datepicker.DateState
import lnx.jetitable.ui.icons.google.CalendarMonth
import lnx.jetitable.ui.icons.google.Mood
import lnx.jetitable.ui.icons.google.Warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassScheduleCard(
    classList: DataState<out List<ClassUiData>>,
    dateState: DateState,
    onPresenceVerify: (ClassUiData) -> Unit,
    onDateUpdate: (Calendar) -> Unit,
    onBackwardDateShift: () -> Unit,
    onForwardDateShift: () -> Unit,
    connectionState: DataState<out Boolean>
) {
    ScheduleCard(
        icon = CalendarMonth,
        title = {
            DatePickerView(
                datePickerState = dateState.datePickerState,
                formattedDate = dateState.formattedDate,
                onDateSelected = onDateUpdate,
                connectionState = connectionState
            )
        },
        additionalTitleContent = {
            CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
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
    ) {
        AnimatedContent(
            targetState = classList
        ) {
            Column {
                when (it) {
                    is DataState.Empty -> {
                        ScheduleStatus(
                            icon = Mood,
                            text = R.string.no_classes
                        )
                    }
                    is DataState.Loading -> {
                        ScheduleStatus(text = R.string.getting_list)
                    }
                    is DataState.Success -> {
                        it.data.forEachIndexed { index, item ->
                            val cardColors = if (item.isNow) {
                                MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHigh to MaterialTheme.colorScheme.onSurface
                            }
                            val time = "${item.start}\n${item.end}"
                            val room = if (item.room.isNotBlank()) {
                                "${stringResource(id = R.string.class_room, item.room)}\n"
                            } else ""
                            val classNumber = "${stringResource(R.string.class_number, item.number)}\n"
                            val classGroup = "${stringResource(id = R.string.class_group, item.group)}\n"
                            val educator = stringResource(id = R.string.educator, item.educator)

                            ScheduleRow(
                                time = time,
                                title = item.name,
                                type = item.type,
                                meetingUrl = item.meetingLink,
                                onMeetingUrlClick = { onPresenceVerify(item) },
                                moodleUrl = item.moodleLink,
                                cardColors = cardColors,
                                expandedText = classNumber + room + classGroup + educator,
                                elementIndex = index,
                                isLastElement = index == it.data.size - 1
                            )
                        }
                    }
                    is DataState.Error -> {
                        ScheduleStatus(
                            icon = Warning,
                            text = it.messageResId
                        )
                    }
                }
            }
        }
    }
}