package lnx.jetitable.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScheduleCard(
    expanded: Boolean = true,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        title()
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) { content() }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleRow(
    firstText: String,
    secondText: String,
    thirdText: String? = null,
    expandedText: String? = null,
    meetingUrl: String,
    moodleUrl: String? = null,
    meetingUrlIcon: ImageVector,
    elementIndex: Int,
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
) {
    var expanded by remember { mutableStateOf(false) }
    val localUriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current

    if (elementIndex > 0) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceContainer,
            thickness = 2.dp
        )
    }

    Box(
        modifier = Modifier
            .background(backgroundColor)
            .clickable { expanded = !expanded }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = firstText,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = secondText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            if (thirdText != null) {
                Text(
                    text = thirdText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (meetingUrl.isNotEmpty()) {
                CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .size(40.dp)
                            .combinedClickable(
                                onClick = {
                                    localUriHandler.openUri(meetingUrl)
                                    onClick()
                                },
                                onLongClick = {
                                    clipboardManager.setText(AnnotatedString(meetingUrl))
                                }
                            )
                    ) {
                        Icon(
                            imageVector = meetingUrlIcon,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            if (!moodleUrl.isNullOrBlank()) {
                CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier
                            .size(40.dp)
                            .combinedClickable(
                                onClick = {
                                    localUriHandler.openUri(meetingUrl)
                                },
                                onLongClick = {
                                    clipboardManager.setText(AnnotatedString(moodleUrl))
                                }
                            )
                    ) {
                        Icon(
                            imageVector = lnx.jetitable.ui.icons.Moodle,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }
        }
    }

    if (expandedText != null) {
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
            modifier = Modifier.background(backgroundColor)
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = expandedText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun StudentScheduleTitle(icon: ImageVector, title: @Composable () -> Unit, content: @Composable () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 4.dp)
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) { title() }

        content()
    }
}

@Composable
fun StudentSchedule(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessHigh))
            .padding(bottom = 16.dp)
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) { content() }
    }
}


fun getMeetingIcon(url: String): ImageVector {
    return when {
        url.contains("zoom.us") -> lnx.jetitable.ui.icons.ZoomMeeting
        url.contains("meet.google.com") -> lnx.jetitable.ui.icons.GoogleMeet
        url.contains("teams.microsoft.com") -> lnx.jetitable.ui.icons.MsTeams
        else -> lnx.jetitable.ui.icons.google.Link
    }
}