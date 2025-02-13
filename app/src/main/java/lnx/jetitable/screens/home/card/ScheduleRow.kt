package lnx.jetitable.screens.home.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp

@Composable
fun ScheduleRow(
    time: String,
    title: String,
    type: String? = null,
    expandedText: String? = null,
    meetingUrl: String,
    moodleUrl: String? = null,
    elementIndex: Int,
    onClick: () -> Unit,
    backgroundColor: Color,
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
                text = time,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            if (type != null) {
                Text(
                    text = type,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (meetingUrl.isNotEmpty()) {
                SiteButton(
                    url = meetingUrl,
                    icon = getMeetingIcon(meetingUrl),
                    color = MaterialTheme.colorScheme.secondary,
                    uriHandler = localUriHandler,
                    clipboardManager = clipboardManager
                ) { onClick() }
            }

            if (!moodleUrl.isNullOrBlank()) {
                SiteButton(
                    url = moodleUrl,
                    icon = lnx.jetitable.ui.icons.Moodle,
                    color = MaterialTheme.colorScheme.tertiary,
                    uriHandler = localUriHandler,
                    clipboardManager = clipboardManager,
                    onClick = {}
                )
            }
        }
    }

    if (expandedText != null) {
        AnimatedVisibility(
            visible = expanded,

            modifier = Modifier
                .background(backgroundColor)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp)
            ) {
                Text(
                    text = expandedText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
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