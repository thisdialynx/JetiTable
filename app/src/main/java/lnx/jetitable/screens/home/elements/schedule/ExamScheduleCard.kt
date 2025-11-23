package lnx.jetitable.screens.home.elements.schedule

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.misc.DataState
import lnx.jetitable.screens.home.elements.SiteButton
import lnx.jetitable.ui.components.StateStatus
import lnx.jetitable.ui.icons.google.ContractEdit
import lnx.jetitable.ui.icons.google.Info
import lnx.jetitable.ui.icons.google.Mood

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScheduleCard(
    examList: DataState<out List<ExamNetworkData>>
) {
    val localUriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboard.current
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardTitle(
                expanded = expanded,
                onCardExpanded = { expanded = !expanded }
            )
            AnimatedVisibility(
                visible = expanded
            ) {
                Content(localUriHandler, clipboardManager, examList)
            }
        }
    }
}

@Composable
private fun CardTitle(
    expanded: Boolean,
    onCardExpanded: () -> Unit,
) {
    val arrowRotation = animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ContractEdit,
            contentDescription = null,
            modifier = Modifier.padding(start = 4.dp, end = 8.dp)
        )
        Text(
            text = stringResource(R.string.exam_schedule),
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onCardExpanded
        ) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                modifier = Modifier.graphicsLayer {
                    this.rotationZ = arrowRotation.value
                },
                contentDescription = null
            )
        }
    }
}

@Composable
private fun Content(
    localUriHandler: UriHandler,
    clipboardManager: Clipboard,
    examList: DataState<out List<ExamNetworkData>>
) {
    val targetColor = if (examList is DataState.Success)
        MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp) else
        MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = targetColor
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        AnimatedContent(
            targetState = examList
        ) { examList ->
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (examList) {
                    is DataState.Empty -> {
                        StateStatus(
                            icon = Mood,
                            description = stringResource(R.string.no_exams)
                        )
                    }
                    is DataState.Loading -> {
                        StateStatus(
                            description = stringResource(R.string.getting_list)
                        )
                    }
                    is DataState.Success -> {
                        examList.data.forEachIndexed { index, examItem ->
                            ContentRow(
                                index = index,
                                isLastElement = index == examList.data.size - 1,
                                examData = examItem,
                                localUriHandler = localUriHandler,
                                clipboardManager = clipboardManager
                            )
                        }
                    }
                    is DataState.Error -> {
                        StateStatus(
                            icon = Info,
                            description = stringResource(examList.messageResId)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ContentRow(
    index: Int,
    isLastElement: Boolean,
    examData: ExamNetworkData,
    localUriHandler: UriHandler,
    clipboardManager: Clipboard,
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = getCardShape(index, isLastElement)

    SharedTransitionLayout {
        AnimatedContent(
            targetState = expanded,
            label = "row_transition"
        ) { targetState ->
            if (!targetState) {
                MainContent(
                    data = examData,
                    shape = shape,
                    localUriHandler = localUriHandler,
                    clipboardManager = clipboardManager,
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    onClick = { expanded = true },
                )
            } else {
                DetailsContent(
                    data = examData,
                    shape = shape,
                    localUriHandler = localUriHandler,
                    clipboardManager = clipboardManager,
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    onClick = { expanded = false },
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MainContent(
    data: ExamNetworkData,
    shape: RoundedCornerShape,
    localUriHandler: UriHandler,
    clipboardManager: Clipboard,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    onClick: () -> Unit,
) {
    with(sharedTransitionScope) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
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
                        text = data.date.dropLast(5),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.sharedBounds(
                            rememberSharedContentState("date"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                        ),
                    )
                    Text(
                        text = data.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .weight(1f)
                            .sharedBounds(
                                rememberSharedContentState("title"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                            ),
                    )

                    if (data.url.isNotBlank()) {
                        SiteButton(
                            url = data.url,
                            icon = getMeetingIcon(data.url),
                            color = MaterialTheme.colorScheme.secondary,
                            uriHandler = localUriHandler,
                            clipboardManager = clipboardManager,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState("meeting"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailsContent(
    data: ExamNetworkData,
    shape: RoundedCornerShape,
    localUriHandler: UriHandler,
    clipboardManager: Clipboard,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    onClick: () -> Unit,
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
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
            onClick = onClick
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = data.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState("title"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                        ),
                )
                Text(
                    text = data.date,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.sharedBounds(
                        rememberSharedContentState("date"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                    ),
                )
                Text(
                    text = data.time,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.sharedBounds(
                        rememberSharedContentState("time"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (data.url.isNotBlank()) {
                    SiteButton(
                        url = data.url,
                        icon = getMeetingIcon(data.url),
                        color = MaterialTheme.colorScheme.secondary,
                        uriHandler = localUriHandler,
                        clipboardManager = clipboardManager,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState("meeting"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                }
            }
        }
    }
}