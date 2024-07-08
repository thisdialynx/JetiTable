package lnx.jetitable.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import lnx.jetitable.R
import lnx.jetitable.core.About
import lnx.jetitable.core.Settings
import lnx.jetitable.timetable.data.DailySchedules
import lnx.jetitable.timetable.data.Schedule
import lnx.jetitable.timetable.data.Subject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.welcome_title)
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Settings.route) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                    IconButton(onClick = { navController.navigate(About.route) }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "About"
                        )
                    }
                }
            )
        }
    ) {
        paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {

            val todaySchedule = DailySchedules.first()
            NextSubjectCard(subject = todaySchedule.subjects.first())
            DailyScheduleCard(schedule = todaySchedule)
        }
    }
}

@Composable
fun NextSubjectCard(subject: Subject) {
    val subjectTypeText = subject.type
    val subjectNameText = subject.name
    val startTime = subject.startTime
    val highlightText = buildAnnotatedString {
        val builtText = stringResource(id = R.string.next_learning_activity,
            subjectNameText, subjectTypeText, startTime)

        val subjectNameStartIndex = builtText.indexOf(subjectNameText)
        val subjectNameEndIndex = subjectNameStartIndex + subjectNameText.length
        addStyle(SpanStyle(color = MaterialTheme.colorScheme.primary), subjectNameStartIndex, subjectNameEndIndex)

        val subjectTypeStartIndex = builtText.indexOf(subjectTypeText)
        val subjectTypeEndIndex = subjectTypeStartIndex + subjectTypeText.length
        addStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary), subjectTypeStartIndex, subjectTypeEndIndex)

        val startTimeStartIndex = builtText.indexOf(startTime)
        val startTimeEndIndex = startTimeStartIndex + startTime.length
        addStyle(SpanStyle(color = MaterialTheme.colorScheme.tertiary), startTimeStartIndex, startTimeEndIndex)
        append(builtText)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        )
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = highlightText
        )
    }
}

@Composable
fun DailyScheduleCard(schedule: Schedule) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
    ) {
        Row {
            Text(
                text = stringResource(id = R.string.schedule_for_day, schedule.date),
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(schedule.subjects) { subject ->
                ScheduleRow(subject = subject)
            }
        }

    }
}

@Composable
fun ScheduleRow(subject: Subject) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${subject.startTime}-${subject.endTime}",
            fontSize = 14.sp,
            modifier = Modifier.weight(0.5f)
        )
        Text(
            text = subject.name,
            modifier = Modifier.weight(1f),

            )
        Text(
            text = subject.type,
            modifier = Modifier.weight(0.1f),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(navController = NavHostController(LocalContext.current))
}