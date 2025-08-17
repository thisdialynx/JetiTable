package lnx.jetitable.screens.home.elements.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import lnx.jetitable.screens.home.elements.SiteButton
import lnx.jetitable.ui.icons.GoogleMeet
import lnx.jetitable.ui.icons.Moodle
import lnx.jetitable.ui.icons.MsTeams
import lnx.jetitable.ui.icons.ZoomMeeting
import lnx.jetitable.ui.icons.google.Link

@Composable
fun ScheduleRow(
    time: String,
    title: String,
    type: String? = null,
    expandedText: String? = null,
    meetingUrl: String,
    moodleUrl: String? = null,
    elementIndex: Int,
    isLastElement: Boolean,
    onClick: () -> Unit,
    backgroundColor: Color,
) {
    var expanded by remember { mutableStateOf(false) }
    val localUriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboard.current

    val topStart = if (elementIndex == 0) 12.dp else 4.dp
    val topEnd = if (elementIndex == 0) 12.dp else 4.dp
    val bottomStart = if (isLastElement) 12.dp else 4.dp
    val bottomEnd = if (isLastElement) 12.dp else 4.dp

    val shape = RoundedCornerShape(
        topStart = topStart,
        topEnd = topEnd,
        bottomStart = bottomStart,
        bottomEnd = bottomEnd
    )

    if (elementIndex > 0) Spacer(modifier = Modifier.height(2.dp))

    Surface(
        color = backgroundColor,
        onClick = { expanded = !expanded },
        shape = shape,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
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
                        icon = Moodle,
                        color = MaterialTheme.colorScheme.tertiary,
                        uriHandler = localUriHandler,
                        clipboardManager = clipboardManager,
                        onClick = {}
                    )
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
    }
}

fun getMeetingIcon(url: String): ImageVector {
    return when {
        url.contains("zoom.us") -> ZoomMeeting
        url.contains("meet.google.com") -> GoogleMeet
        url.contains("teams.microsoft.com") -> MsTeams
        else -> Link
    }
}