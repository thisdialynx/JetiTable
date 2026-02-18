@file:OptIn(ExperimentalMaterial3Api::class)

package lnx.jetitable.screens.home.elements.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.api.timetable.data.query.AttendanceData
import lnx.jetitable.misc.DataState
import lnx.jetitable.ui.components.StateStatus
import lnx.jetitable.ui.icons.google.Draft
import lnx.jetitable.ui.icons.google.Info

@Composable
fun AttendanceDialog(
    isOpen: Boolean,
    list: DataState<List<AttendanceData>>,
    studentFullName: String,
    onDismissRequest: () -> Unit,
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.List,
                    contentDescription = null
                )
            },
            title = {
                Text(
                    text = stringResource(R.string.attendance_log)
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    when (list) {
                        is DataState.Empty -> {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                StateStatus(
                                    icon = Draft,
                                    description = stringResource(R.string.empty_list),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        is DataState.Loading -> {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme
                                        .surfaceColorAtElevation(2.dp)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                StateStatus(
                                    description = stringResource(R.string.loading)
                                )
                            }
                        }
                        is DataState.Success -> {
                            val lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                            val lineHeightDp = with(LocalDensity.current) { lineHeight.toDp() }
                            val minRowHeight = lineHeightDp * 2

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 480.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                            ) {
                                itemsIndexed(list.data) { index, data ->
                                    val isLast = list.data.size - 1 == index
                                    val shape = getCardShape(index, isLast)
                                    var isExpanded by remember { mutableStateOf(false) }
                                    val groupStr = if (data.group.isEmpty()) "" else stringResource(R.string.group, data.group) + "\n"
                                    val extendedInfoStr = "${stringResource(R.string.status, data.role)}\n" +
                                            groupStr + stringResource(R.string.joins, data.joins)
                                    val surfaceColors = when {
                                        data.role == "викладач" -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
                                        data.fullName == studentFullName -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
                                        else -> MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp) to MaterialTheme.colorScheme.onSurface
                                    }

                                    Surface(
                                        shape = shape,
                                        color = surfaceColors.first,
                                        contentColor = surfaceColors.second,
                                        onClick = { isExpanded = !isExpanded },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.heightIn(min = minRowHeight)
                                            ) {
                                                Text(
                                                    text = index.toString()
                                                )
                                                Text(
                                                    text = data.fullName,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Text(
                                                    text = data.time
                                                )
                                            }
                                            AnimatedVisibility(
                                                visible = isExpanded
                                            ) {
                                                Column(
                                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                                    modifier = Modifier.padding(horizontal = 16.dp)
                                                ) {
                                                    Text(extendedInfoStr)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else -> {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme
                                        .surfaceColorAtElevation(2.dp)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                StateStatus(
                                    icon = Info,
                                    description = stringResource(R.string.failed_to_fetch_data)
                                )
                            }
                        }
                    }
                }

            },
            confirmButton = {},
            dismissButton = {
                OutlinedButton(
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = stringResource(R.string.dismiss)
                    )
                }
            },
        )
    }
}