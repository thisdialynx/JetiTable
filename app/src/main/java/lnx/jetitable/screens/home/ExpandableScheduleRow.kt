package lnx.jetitable.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.misc.isLessonNow
import lnx.jetitable.timetable.api.query.data.Lesson

@Composable
fun ExpandableScheduleRow(lesson: Lesson, index: Int, homeViewModel: HomeViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val localUriHandler = LocalUriHandler.current

    val meetingIcon = when {
        lesson.loadZoom.contains("zoom.us") -> lnx.jetitable.ui.icons.ZoomMeeting
        lesson.loadZoom.contains("meet.google.com") -> lnx.jetitable.ui.icons.GoogleMeet
        lesson.loadZoom.contains("teams.microsoft.com") -> lnx.jetitable.ui.icons.MsTeams
        else -> lnx.jetitable.ui.icons.google.Link
    }
    val bgColor = if (isLessonNow(lesson)) MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp) else MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)

    if (index > 0) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
            thickness = 2.dp
        )
    }
    Box(
        modifier = Modifier
            .background(bgColor)
            .clickable { expanded = !expanded }
    ) {
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
            if (lesson.loadZoom.isNotEmpty()) {
                CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                    Card(
                        onClick = {
                            localUriHandler.openUri(lesson.loadZoom)
                            homeViewModel.verifyPresence(lesson)
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = meetingIcon,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }

            if (lesson.loadMoodleStudent.isNotEmpty()) {
                CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                    Card(
                        onClick = { localUriHandler.openUri(lesson.loadMoodleStudent) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        ),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = lnx.jetitable.ui.icons.Moodle,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp),
                            tint = MaterialTheme.colorScheme.onTertiary
                        )
                    }
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
                text = "${stringResource(id = R.string.lesson_number, lesson.numLesson)}\n" +
                        "${stringResource(id = R.string.lesson_group, lesson.group)}\n" +
                        stringResource(id = R.string.lesson_teacher, lesson.fio),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}