package lnx.jetitable.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import lnx.jetitable.R
import lnx.jetitable.navigation.About
import lnx.jetitable.navigation.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val homeViewModel: HomeViewModel = viewModel()
    val context = LocalContext.current
    val lessonsList = homeViewModel.dailyLessonList
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val selectedDate = homeViewModel.selectedDate

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.welcome_title)) },
                actions = {
                    IconButton(onClick = { navController.navigate(Settings.route) }) {
                        Icon(
                            imageVector = lnx.jetitable.ui.icons.google.Settings,
                            contentDescription = "Settings"
                        )
                    }
                    IconButton(onClick = { navController.navigate(About.route) }) {
                        Icon(
                            imageVector = lnx.jetitable.ui.icons.google.Info,
                            contentDescription = "About"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                ) {
                    Icon(
                        imageVector = lnx.jetitable.ui.icons.google.CalendarMonth,
                        contentDescription = "",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.schedule_for_day),
                        style = MaterialTheme.typography.titleMedium
                    )
                    DatePickerExtended(
                        selectedDate = selectedDate,
                        modifier = Modifier.weight(1f)
                    ) { year, month, day ->
                        homeViewModel.onDateSelected(year, month, day)
                    }
                    CompositionLocalProvider(value = LocalMinimumInteractiveComponentEnforcement provides false) {
                        IconButton(
                            onClick = { homeViewModel.shiftDate(-1) },
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                contentDescription = "Next date"
                            )
                        }
                        IconButton(onClick = { homeViewModel.shiftDate(1) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = "Previous date",
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessMedium)),
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
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Icon(imageVector = lnx.jetitable.ui.icons.google.Mood, contentDescription = "")
                                    Text(
                                        text = stringResource(id = R.string.no_lessons),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                            }
                            else -> {
                                lessonsList.lessons.forEachIndexed { index, lesson ->
                                    ExpandableScheduleRow(lesson = lesson, index = index, homeViewModel = homeViewModel)
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
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(navController = NavHostController(LocalContext.current))
}