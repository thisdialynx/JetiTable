package lnx.jetitable.screens.notifications.cards

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import lnx.jetitable.R

@Composable
fun EventOptionsCard(
    title: String,
    enabled: Boolean,
    checked: Boolean,
    selectedMinutes: Int,
    onCheckedChange: (Boolean) -> Unit,
    onTimeSelected: (Int) -> Unit
) {
    var isTimePickerDropdownOpened by remember { mutableStateOf(false) }
    val timeOptions = listOf(
        5 to stringResource(R.string.before_event_minutes, 5),
        10 to stringResource(R.string.before_event_minutes, 10),
        15 to stringResource(R.string.before_event_minutes, 15),
        30 to stringResource(R.string.before_event_minutes, 30),
        45 to stringResource(R.string.before_event_minutes, 45)
    )

    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    enabled = enabled
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            EventCardSuboptionRow(
                isDropdownMenuOpen = isTimePickerDropdownOpened,
                onDropdownButtonPressed = { isTimePickerDropdownOpened = !isTimePickerDropdownOpened },
                dropdownMenuButtonLabel = stringResource(R.string.before_event_minutes, selectedMinutes.toString()),
                enabled = checked,
                options = timeOptions,
                selectedOption = selectedMinutes,
                onOptionSelected = onTimeSelected,
                optionTitle = stringResource(R.string.time_before_card_title),
                icon = lnx.jetitable.ui.icons.google.Schedule
            )
        }
    }
}

@Composable
fun EventCardSuboptionRow(
    isDropdownMenuOpen: Boolean,
    onDropdownButtonPressed: () -> Unit,
    dropdownMenuButtonLabel: String,
    enabled: Boolean,
    options: List<Pair<Int, String>>,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    optionTitle: String,
    icon: ImageVector
    ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = optionTitle,
            modifier = Modifier.padding(start = 8.dp)
        )
        Text(
            text = optionTitle,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box {
            val arrowRotation by animateFloatAsState(
                targetValue = if (isDropdownMenuOpen) 180f else 0f
            )

            TextButton(
                onClick = onDropdownButtonPressed,
                enabled = enabled,
                colors = ButtonDefaults.textButtonColors(
                    disabledContentColor = MaterialTheme.colorScheme.onSurface,
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = dropdownMenuButtonLabel,
                    modifier = Modifier.padding(start = 6.dp)
                )
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }
            DropdownMenu(
                expanded = isDropdownMenuOpen,
                onDismissRequest = onDropdownButtonPressed,
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
            ) {
                Column {
                    options.forEach {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            RadioButton(
                                selected = selectedOption == it.first,
                                onClick = { onOptionSelected(it.first) }
                            )
                            Text(
                                text = it.second
                            )
                        }
                    }
                }
            }
        }
    }
}