package lnx.jetitable.screens.home.elements.schedule

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.misc.DataState
import lnx.jetitable.ui.icons.google.ContractEdit
import lnx.jetitable.ui.icons.google.Mood
import lnx.jetitable.ui.icons.google.Warning

@Composable
fun ExamScheduleCard(examList: DataState<out List<ExamNetworkData>>) {
    var expanded by remember { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    ScheduleCard(
        expanded = expanded,
        icon = ContractEdit,
        title = {
            Text(
                text = stringResource(id = R.string.exam_schedule),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        },
        additionalTitleContent = {
            CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(arrowRotation)
                    )
                }
            }
        }
    ) {
        AnimatedContent(
            targetState = examList
        ) {
            when (it) {
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
                        text = it.messageResId
                    )
                }
                is DataState.Success -> {
                    it.data.forEachIndexed { index, item ->
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
                            isLastElement = index == it.data.size - 1
                        )
                    }
                }
            }
        }

    }
}