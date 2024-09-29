package lnx.jetitable.screens.home

import android.icu.util.Calendar
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import lnx.jetitable.R
import lnx.jetitable.timetable.api.query.data.DailyLessonListResponse
import lnx.jetitable.viewmodel.HomeViewModel

@Composable
fun ScheduleCard(lessonList: DailyLessonListResponse?, selectedDate: Calendar, homeViewModel: HomeViewModel) {
    val time = Calendar.getInstance().timeInMillis
    var currentTime by remember { mutableLongStateOf(time) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Calendar.getInstance().timeInMillis
            delay(60000)
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        ScheduleCardTitle(selectedDate = selectedDate, homeViewModel = homeViewModel)
        ScheduleInnerCard(lessonList = lessonList, currentTime = currentTime, homeViewModel = homeViewModel)
    }
}

@Composable
fun ScheduleInnerCard(lessonList: DailyLessonListResponse?, currentTime: Long, homeViewModel: HomeViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessMedium)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    LazyColumn {
                        itemsIndexed(lessonList.lessons) { index, lesson ->
                            ExpandableScheduleRow(lesson = lesson, index = index, currentTime = currentTime, homeViewModel = homeViewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleCardTitle(selectedDate: Calendar, homeViewModel: HomeViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp).fillMaxWidth()
    ) {
        Icon(
            imageVector = lnx.jetitable.ui.icons.google.CalendarMonth,
            contentDescription = null,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = stringResource(id = R.string.schedule_for_day),
            style = MaterialTheme.typography.titleMedium
        )
        DatePickerExtended(
            selectedDate = selectedDate,
            modifier = Modifier.weight(1f)
        ) { year, month, day -> homeViewModel.onDateSelected(year, month, day) }

        CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            IconButton(
                onClick = { homeViewModel.shiftDate(-1) },
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = null
                )
            }
            IconButton(onClick = { homeViewModel.shiftDate(1) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    }
}