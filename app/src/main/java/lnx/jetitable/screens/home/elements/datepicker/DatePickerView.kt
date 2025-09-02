package lnx.jetitable.screens.home.elements.datepicker

import android.icu.util.Calendar
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.misc.ConnectionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerView(
    formattedDate: String,
    datePickerState: DatePickerState,
    onDateSelected: (Calendar) -> Unit,
    connectionState: ConnectionState
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            val newDate = Calendar.getInstance().apply { timeInMillis = millis }
                            onDateSelected(newDate)
                        }
                    },
                    modifier = Modifier.padding(end = 6.dp)
                ) { Text(text = "OK") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) {
                    Text(text = stringResource(id = R.string.dismiss))
                }
            }
        ) { DatePicker(state = datePickerState) }
    }

    CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        val arrowRotation by animateFloatAsState(
            targetValue = if (showDialog) 180f else 0f
        )

        Surface(
            onClick = { showDialog = true },
            shape = RoundedCornerShape(8.dp),
            color = Color.Transparent,
            enabled = connectionState == ConnectionState.Available
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = formattedDate,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }
        }
    }
}