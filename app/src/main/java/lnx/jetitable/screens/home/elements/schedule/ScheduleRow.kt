package lnx.jetitable.screens.home.elements.schedule

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import lnx.jetitable.screens.home.elements.SiteButton
import lnx.jetitable.ui.icons.GoogleMeet
import lnx.jetitable.ui.icons.Moodle
import lnx.jetitable.ui.icons.MsTeams
import lnx.jetitable.ui.icons.ZoomMeeting
import lnx.jetitable.ui.icons.google.Link

@OptIn(ExperimentalSharedTransitionApi::class)
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
    onMeetingUrlClick: () -> Unit = {},
    cardColors: Pair<Color, Color>,
) {
    var expanded by remember { mutableStateOf(false) }
    val localUriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboard.current
    val shape = getCardShape(elementIndex, isLastElement)
    if (elementIndex > 0) Spacer(modifier = Modifier.height(2.dp))

    SharedTransitionLayout {
        AnimatedContent(
            targetState = expanded,
            label = "row_transition"
        ) { targetState ->
            if (!targetState) {
                MainContent(
                    time = time,
                    title = title,
                    type = type,
                    meetingUrl = meetingUrl,
                    onMeetingUrlClick = onMeetingUrlClick,
                    moodleUrl = moodleUrl,
                    onClick = { expanded = true },
                    cardColors = cardColors,
                    shape = shape,
                    localUriHandler = localUriHandler,
                    clipboardManager = clipboardManager,
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            } else {
                DetailsContent(
                    onBack = { expanded = false },
                    time = time,
                    title = title,
                    type = type,
                    expandedText = expandedText,
                    meetingUrl = meetingUrl,
                    onMeetingUrlClick = onMeetingUrlClick,
                    moodleUrl = moodleUrl,
                    shape = shape,
                    cardColors = cardColors,
                    localUriHandler = localUriHandler,
                    clipboardManager = clipboardManager,
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun MainContent(
    time: String,
    title: String,
    type: String? = null,
    meetingUrl: String,
    onMeetingUrlClick: () -> Unit,
    moodleUrl: String? = null,
    onClick: () -> Unit,
    cardColors: Pair<Color, Color>,
    shape: RoundedCornerShape,
    localUriHandler: UriHandler,
    clipboardManager: Clipboard,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope
) {
    with(sharedTransitionScope) {
        Surface(
            color = cardColors.first,
            onClick = onClick,
            shape = shape,
            modifier = Modifier.sharedElement(
                rememberSharedContentState("surface"),
                animatedVisibilityScope = animatedVisibilityScope,
            )
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
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState("time"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                        color = cardColors.second
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .weight(1f)
                            .sharedBounds(
                                rememberSharedContentState("title"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                            ),
                        color = cardColors.second
                    )
                    type?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.sharedBounds(
                                rememberSharedContentState("type"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                            ),
                            color = cardColors.second
                        )
                    }

                    if (meetingUrl.isNotEmpty()) {
                        SiteButton(
                            url = meetingUrl,
                            icon = getMeetingIcon(meetingUrl),
                            color = MaterialTheme.colorScheme.secondary,
                            uriHandler = localUriHandler,
                            clipboardManager = clipboardManager,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState("meeting"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                            onButtonClick = onMeetingUrlClick
                        )
                    }

                    if (!moodleUrl.isNullOrBlank()) {
                        SiteButton(
                            url = moodleUrl,
                            icon = Moodle,
                            color = MaterialTheme.colorScheme.tertiary,
                            uriHandler = localUriHandler,
                            clipboardManager = clipboardManager,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState("moodle"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun DetailsContent(
    onBack: () -> Unit,
    expandedText: String?,
    time: String,
    title: String,
    type: String? = null,
    meetingUrl: String,
    onMeetingUrlClick: () -> Unit,
    shape: RoundedCornerShape,
    localUriHandler: UriHandler,
    clipboardManager: Clipboard,
    moodleUrl: String? = null,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    cardColors: Pair<Color, Color>
) {
    with(sharedTransitionScope) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .sharedElement(
                    rememberSharedContentState("surface"),
                    animatedVisibilityScope = animatedVisibilityScope
                ),
            shape = shape,
            color = cardColors.first,
            onClick = onBack
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = time,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState("time"),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                        color = cardColors.second
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .weight(1f)
                            .sharedBounds(
                                rememberSharedContentState("title"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                            ),
                        color = cardColors.second
                    )
                    type?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.sharedBounds(
                                rememberSharedContentState("type"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                            ),
                            color = cardColors.second
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedVisibility(
                    visible = expandedText != null
                ) {
                    expandedText?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = cardColors.second
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (meetingUrl.isNotBlank()) {
                        SiteButton(
                            url = meetingUrl,
                            icon = getMeetingIcon(meetingUrl),
                            color = MaterialTheme.colorScheme.secondary,
                            uriHandler = localUriHandler,
                            clipboardManager = clipboardManager,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState("meeting"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                            onButtonClick = onMeetingUrlClick
                        )
                    }

                    if (!moodleUrl.isNullOrBlank()) {
                        SiteButton(
                            url = moodleUrl,
                            icon = Moodle,
                            color = MaterialTheme.colorScheme.tertiary,
                            uriHandler = localUriHandler,
                            clipboardManager = clipboardManager,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState("moodle"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
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

fun getCardShape(index: Int, isLastElement: Boolean): RoundedCornerShape {
    val roundedCorner = 12.dp
    val semiRounded = 4.dp
    return when {
        index == 0 && !isLastElement -> {
            RoundedCornerShape(
                topStart = roundedCorner,
                topEnd = roundedCorner,
                bottomStart = semiRounded,
                bottomEnd = semiRounded
            )
        }
        index != 0 && isLastElement -> {
            RoundedCornerShape(
                topStart = semiRounded,
                topEnd = semiRounded,
                bottomStart = roundedCorner,
                bottomEnd = roundedCorner
            )
        }
        index != 0 && !isLastElement -> {
            RoundedCornerShape(
                topStart = semiRounded,
                topEnd = semiRounded,
                bottomStart = semiRounded,
                bottomEnd = semiRounded
            )
        }
        else -> {
            RoundedCornerShape(
                topStart = roundedCorner,
                topEnd = roundedCorner,
                bottomStart = roundedCorner,
                bottomEnd = roundedCorner
            )
        }
    }
}