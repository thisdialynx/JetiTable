package lnx.jetitable.screens.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import lnx.jetitable.R
import lnx.jetitable.misc.isLessonNow
import lnx.jetitable.navigation.Settings
import lnx.jetitable.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val homeViewModel: HomeViewModel = viewModel()
    val userUiState by homeViewModel.userInfoFlow.collectAsStateWithLifecycle()
    val currentTime by homeViewModel.currentTimeFlow.collectAsStateWithLifecycle(0L)
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
                    IconButton(onClick = { navController.navigate(Settings.route) }) {
                        Icon(
                            imageVector = lnx.jetitable.ui.icons.google.Settings,
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
                            title = {
                                DatePickerView(
                                    selectedDate = userUiState.selectedDate
                                ) { date -> homeViewModel.onDateSelected(date) }
                            }
                        ) {
                            CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                                IconButton(
                                    onClick = { homeViewModel.shiftDay(-1) },
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                        contentDescription = null
                                    )
                                }

                                IconButton(onClick = { homeViewModel.shiftDay(1) }) {
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
                        userUiState.let {
                            val lessons = it.lessonList

                            when {
                                lessons == null -> {
                                    ListStatusView(text = R.string.getting_lists)
                                }

                                lessons.isEmpty() -> {
                                    ListStatusView(icon = lnx.jetitable.ui.icons.google.Mood, text = R.string.no_lessons)
                                }

                                else -> {
                                    lessons.forEachIndexed { index, lesson ->
                                        val bgColor = if (isLessonNow(lesson, currentTime)) {
                                            MaterialTheme.colorScheme.primaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.surfaceContainerHigh
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
            }
            item {
                var expanded by remember { mutableStateOf(false) }
                ScheduleCard(
                    expanded = expanded,
                    title = {
                        StudentScheduleTitle(
                            icon = lnx.jetitable.ui.icons.google.ContractEdit,
                            title = {
                                Text(text = stringResource(id = R.string.session_schedule))
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
                    StudentSchedule {
                        userUiState.let {
                            val sessions = it.sessionList

                            when {
                                sessions == null -> {
                                    ListStatusView(text = R.string.getting_lists)
                                }
                                sessions.isEmpty() -> {
                                    ListStatusView(icon = lnx.jetitable.ui.icons.google.Mood, text = R.string.no_session)
                                }
                                else -> {
                                    sessions.forEachIndexed { index, session ->
                                        ScheduleRow(
                                            firstText = session.lessonTime,
                                            secondText = session.lessonName,
                                            meetingUrl = session.url,
                                            meetingUrlIcon = getMeetingIcon(session.url),
                                            onClick = {},
                                            expandedText = "${stringResource(id = R.string.lesson_number, session.lessonNumber)}\n" +
                                                    "${stringResource(id = R.string.date, session.date)}\n" +
                                                    stringResource(id = R.string.teacher, session.teacher),
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
}

@Composable
fun ListStatusView(icon: ImageVector? = null, text: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null)
        } else {
            CircularProgressIndicator(
                strokeCap = StrokeCap.Round,
                modifier = Modifier.then(Modifier.size(16.dp))
            )
        }
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}