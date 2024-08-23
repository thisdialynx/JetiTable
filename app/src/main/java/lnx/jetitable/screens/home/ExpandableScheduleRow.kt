package lnx.jetitable.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
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
import androidx.compose.ui.unit.dp
import lnx.jetitable.timetable.api.query.data.Lesson

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