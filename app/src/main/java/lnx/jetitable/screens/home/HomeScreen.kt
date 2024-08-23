package lnx.jetitable.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import lnx.jetitable.R
import lnx.jetitable.navigation.About
import lnx.jetitable.navigation.Settings
import lnx.jetitable.timetable.api.query.data.Lesson
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val homeViewModel: HomeViewModel = viewModel()
    val fullName = homeViewModel.fullName
    val lessonsList = homeViewModel.dailyLessonList

    LaunchedEffect(Unit) {
        homeViewModel.getDailyLessonList()
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.welcome_title, fullName ?: R.string.failed_to_fetch_data)
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Settings.route) }) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = "Settings"
                        )
                    }
                    IconButton(onClick = { navController.navigate(About.route) }) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = "About"
                        )
                    }
                }
            )
        }
    ) {
        paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = lnx.jetitable.ui.icons.google.CalendarMonth, contentDescription = "")
                        Text(
                            text = stringResource(id = R.string.schedule_for_day),
                            style = MaterialTheme.typography.titleMedium
                        )
                        DatePickerExtended()
                    }

                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessHigh)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when {
                            lessonsList == null -> {
                                CircularProgressIndicator(
                                    strokeCap = StrokeCap.Round,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            lessonsList.lessons.isEmpty() -> {
                                Text(
                                    text = stringResource(id = R.string.no_lessons),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            else -> {
                                lessonsList.lessons.forEachIndexed { index, lesson ->
                                    ExpandableScheduleRow(lesson = lesson, index = index)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableScheduleRow(lesson: Lesson, index: Int) {
    var expanded by remember { mutableStateOf(false) }
    val localUriHandler = LocalUriHandler.current

    if (index > 0) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
            thickness = 2.dp
        )
    }
    Column(modifier = Modifier.clickable { expanded = !expanded }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "${lesson.timeBeg}\n${lesson.timeEnd}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = lesson.lesson,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = lesson.type,
                style = MaterialTheme.typography.bodyMedium
            )
            CompositionLocalProvider(value = LocalMinimumInteractiveComponentEnforcement provides false) {
                Card(
                    onClick = { localUriHandler.openUri(lesson.loadZoom) },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                    ),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = when {
                            lesson.loadZoom.contains("zoom.us") -> lnx.jetitable.ui.icons.ZoomMeeting
                            lesson.loadZoom.contains("meet.google.com") -> lnx.jetitable.ui.icons.GoogleMeet
                            lesson.loadZoom.contains("team.microsoft.com") -> lnx.jetitable.ui.icons.MsTeams
                            else -> Icons.Rounded.Link
                        },
                        contentDescription = "",
                        modifier = Modifier.padding(8.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
        
    

    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically(animationSpec = tween(durationMillis = 300)),
        exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${lesson.fio}, ${lesson.group}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )


            if (lesson.loadMoodleStudent.contains("moodle2.snu.edu.ua")) {
                CompositionLocalProvider(value = LocalMinimumInteractiveComponentEnforcement provides false) {
                    Card(
                        onClick = { localUriHandler.openUri(lesson.loadMoodleStudent) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                        ),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = lnx.jetitable.ui.icons.Moodle,
                            contentDescription = "",
                            modifier = Modifier.padding(8.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerExtended() {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {}
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
    Text(
        text = datePickerState.selectedDateMillis?.let { millis ->
            val formatter = remember { DatePickerDefaults.dateFormatter() }
            val locale = Locale.getDefault()
            formatter.formatDate(millis, locale)
        } ?: "[no date selected]",
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.clickable { showDialog = true }
    )
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(navController = NavHostController(LocalContext.current))
}