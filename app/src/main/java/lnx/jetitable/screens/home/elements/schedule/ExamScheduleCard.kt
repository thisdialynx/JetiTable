package lnx.jetitable.screens.home.elements.schedule

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import lnx.jetitable.R
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.misc.DataState
import lnx.jetitable.ui.icons.google.ContractEdit
import lnx.jetitable.ui.icons.google.Mood
import lnx.jetitable.ui.icons.google.Warning

@Composable
fun ExamScheduleCard(examList: DataState<out List<ExamNetworkData>>) {
    var expanded by remember { mutableStateOf(false) }

    ScheduleCard(
        expanded = expanded,
        icon = ContractEdit,
        title = {
            Text(
                text = stringResource(id = R.string.exam_schedule),
                style = MaterialTheme.typography.titleMedium
            )
        },
        additionalTitleContent = {
            CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                IconButton(onClick = { expanded = !expanded }) {
                    if (expanded) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowUp,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    ) {
        when (examList) {
            is DataState.Empty -> {
                ScheduleStatus(
                    icon = Mood,
                    text = R.string.no_exams
                )
            }
            is DataState.Loading -> {
                ScheduleStatus(text = R.string.getting_list)
            }
            is DataState.Error -> {
                ScheduleStatus(
                    icon = Warning,
                    text = examList.messageResId
                )
            }
            is DataState.Success -> {
                examList.data.forEachIndexed { index, item ->
                    ScheduleRow(
                        time = item.time,
                        title = item.name,
                        meetingUrl = item.url,
                        onClick = {},
                        backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        expandedText = "${stringResource(id = R.string.class_number, item.number)}\n" +
                                "${stringResource(id = R.string.date, item.date)}\n" +
                                stringResource(id = R.string.educator, item.educator),
                        elementIndex = index,
                        isLastElement = index == examList.data.size - 1
                    )
                }
            }
        }
    }
}