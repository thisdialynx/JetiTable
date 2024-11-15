package lnx.jetitable.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import lnx.jetitable.R
import lnx.jetitable.misc.isLessonNow
import lnx.jetitable.navigation.About
import lnx.jetitable.navigation.Settings
import lnx.jetitable.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val homeViewModel: HomeViewModel = viewModel()
    val lessonList = homeViewModel.lessonList
    val sessionList = homeViewModel.sessionList
    val selectedDate = homeViewModel.selectedDate
    val currentTime by homeViewModel.currentTimeFlow.collectAsStateWithLifecycle(0L)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.welcome_title)) },
                actions = {
                    IconButton(onClick = { navController.navigate(Settings.route) }) {
                        Icon(
                            imageVector = lnx.jetitable.ui.icons.google.Settings,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = { navController.navigate(About.route) }) {
                        Icon(
                            imageVector = lnx.jetitable.ui.icons.google.Info,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                ScheduleCard(
                    title = {
                        StudentScheduleTitle(
                            icon = lnx.jetitable.ui.icons.google.CalendarMonth,
                            title = stringResource(id = R.string.schedule_for_day)
                        ) {
                            DatePickerExtended(
                                selectedDate = selectedDate
                            ) { year, month, day -> homeViewModel.onDateSelected(year, month, day) }

                            CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                                IconButton(
                                    onClick = { homeViewModel.onDateSelected(shift = -1) },
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                        contentDescription = null
                                    )
                                }

                                IconButton(onClick = { homeViewModel.onDateSelected(shift = 1) }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    },
                ) {
                    StudentSchedule {
                        when {
                            lessonList == null -> {
                                CircularProgressIndicator(
                                    strokeCap = StrokeCap.Round,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            lessonList.lessons.isEmpty() -> {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Icon(imageVector = lnx.jetitable.ui.icons.google.Mood, contentDescription = null)
                                    Text(
                                        text = stringResource(id = R.string.no_lessons),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                            }
                            else -> {
                                lessonList.lessons.forEachIndexed { index, lesson ->
                                    val bgColor = if (isLessonNow(lesson, currentTime)) {
                                        MaterialTheme.colorScheme.primaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)
                                    }

                                    ScheduleRow(
                                        firstText = "${lesson.start}\n${lesson.end}",
                                        secondText = lesson.name,
                                        thirdText = lesson.type,
                                        meetingUrl = lesson.meetingLink,
                                        meetingUrlIcon = getMeetingIcon(lesson.meetingLink),
                                        moodleUrl = lesson.moodleLink,
                                        onClick = { homeViewModel.verifyPresence(lesson) },
                                        backgroundColor = bgColor,
                                        expandedText = "${stringResource(id = R.string.lesson_number, lesson.number)}\n" +
                                                "${stringResource(id = R.string.lesson_group, lesson.group)}\n" +
                                                stringResource(id = R.string.teacher, lesson.teacherFullName),
                                        elementIndex = index
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item {
                ScheduleCard(
                    title = {
                        StudentScheduleTitle(
                            icon = lnx.jetitable.ui.icons.google.ContractEdit,
                            title = stringResource(id = R.string.session_schedule),
                            content = {}
                        )
                    }
                ) {
                    StudentSchedule {
                        when {
                            sessionList == null -> {
                                CircularProgressIndicator(
                                    strokeCap = StrokeCap.Round,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            sessionList.sessions.isEmpty() -> {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Icon(imageVector = lnx.jetitable.ui.icons.google.QuestionMark, contentDescription = null)
                                    Text(
                                        text = stringResource(id = R.string.no_session),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                            }
                            else -> {
                                sessionList.sessions.forEachIndexed { index, session ->
                                    ScheduleRow(
                                        firstText = session.lessonTime,
                                        secondText = session.lessonName,
                                        meetingUrl = session.url,
                                        meetingUrlIcon = getMeetingIcon(session.url),
                                        onClick = {},
                                        backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
                                        expandedText = "${session.date}\n${stringResource(id = R.string.teacher, session.teacher)}",
                                        elementIndex = index
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

