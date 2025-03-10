package lnx.jetitable.screens.home

import android.icu.util.Calendar
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import lnx.jetitable.R
import lnx.jetitable.misc.ConnectionState
import lnx.jetitable.misc.DateState
import lnx.jetitable.screens.home.card.ScheduleCard
import lnx.jetitable.screens.home.card.ScheduleRow
import lnx.jetitable.screens.home.card.ScheduleStatus
import lnx.jetitable.screens.home.card.ScheduleTable
import lnx.jetitable.screens.home.card.ScheduleTitle
import lnx.jetitable.screens.home.data.ClassUiData
import lnx.jetitable.timetable.api.query.data.Exam

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUI(
    dateState: DateState,
    connectivityState: ConnectionState,
    classList: List<ClassUiData>?,
    examList: List<Exam>?,
    onDateUpdate: (Calendar) -> Unit,
    onForwardDateShift: () -> Unit,
    onBackwardDateShift: () -> Unit,
    onPresenceVerify: (ClassUiData) -> Unit,
    onSettingsNavigate: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    var title by rememberSaveable { mutableIntStateOf(R.string.welcome_title) }

    LaunchedEffect(Unit) {
        delay(3000)
        title = R.string.home_screen
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Crossfade(targetState = title, label = "") { currentTitle ->
                        Text(text = stringResource(id = currentTitle))
                    }
                },
                actions = {
                    IconButton(onClick = { onSettingsNavigate() }) {
                        Icon(
                            imageVector = lnx.jetitable.ui.icons.google.Settings,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                ScheduleCard(
                    title = {
                        ScheduleTitle(
                            icon = lnx.jetitable.ui.icons.google.CalendarMonth,
                            title = {
                                DatePickerView(
                                    datePickerState = dateState.datePickerState,
                                    formattedDate = dateState.formattedDate,
                                    onDateSelected = { onDateUpdate(it) }
                                )
                            }
                        ) {
                            CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                                IconButton(
                                    onClick = { onBackwardDateShift() },
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                        contentDescription = null
                                    )
                                }
                                IconButton(onClick = { onForwardDateShift() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    },
                ) {
                    ScheduleTable(
                        connectivityState = connectivityState,
                        emptyContent = {
                            ScheduleStatus(
                                icon = lnx.jetitable.ui.icons.google.Mood,
                                text = R.string.no_classes
                            )
                        },
                        data = classList
                    ) {
                        classList?.forEachIndexed { index, uiClass ->
                            val bgColor = if (uiClass.isNow) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            }
                            val time = "${uiClass.start}\n${uiClass.end}"
                            val room = if (uiClass.room.isNotBlank()) {
                                "${stringResource(id = R.string.class_room, uiClass.room)}\n"
                            } else ""
                            val classNumber = "${stringResource(R.string.class_number, uiClass.number)}\n"
                            val classGroup = "${stringResource(id = R.string.class_group, uiClass.group)}\n"
                            val educator = stringResource(id = R.string.educator, uiClass.educator)

                            ScheduleRow(
                                time = time,
                                title = uiClass.name,
                                type = uiClass.type,
                                meetingUrl = uiClass.meetingLink,
                                moodleUrl = uiClass.moodleLink,
                                onClick = { onPresenceVerify(uiClass) },
                                backgroundColor = bgColor,
                                expandedText = classNumber + room + classGroup + educator,
                                elementIndex = index
                            )
                        }
                    }
                }
            }
            item {
                var expanded by remember { mutableStateOf(false) }
                ScheduleCard(
                    expanded = expanded,
                    title = {
                        ScheduleTitle(
                            icon = lnx.jetitable.ui.icons.google.ContractEdit,
                            title = {
                                Text(text = stringResource(id = R.string.exam_schedule))
                            },
                        ) {
                            CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                                IconButton(onClick = {expanded = !expanded}) {
                                    if (expanded) {
                                        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                                    } else {
                                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                                    }
                                }
                            }
                        }
                    }
                ) {
                    ScheduleTable(
                        connectivityState = connectivityState,
                        emptyContent = {
                            ScheduleStatus(
                                icon = lnx.jetitable.ui.icons.google.Mood,
                                text = R.string.no_exams
                            )
                        },
                        data = examList
                    ) {
                        examList?.forEachIndexed { index, session ->
                            ScheduleRow(
                                time = session.time,
                                title = session.name,
                                meetingUrl = session.url,
                                onClick = {},
                                backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                expandedText = "${stringResource(id = R.string.class_number, session.number)}\n" +
                                        "${stringResource(id = R.string.date, session.date)}\n" +
                                        stringResource(id = R.string.educator, session.educator),
                                elementIndex = index
                            )
                        }
                    }
                }
            }
        }
    }
}