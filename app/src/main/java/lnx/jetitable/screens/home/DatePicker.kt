package lnx.jetitable.screens.home

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.misc.getAcademicYear
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerExtended(context: Context, selectedDate: android.icu.util.Calendar, modifier: Modifier = Modifier, onDateSelected: (Int, Int, Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH) + 1
    val academicYear = getAcademicYear(currentYear, currentMonth)
    val yearRange = academicYear.split("/").map { it.toInt() }
    val dateFormat = "%02d.%02d.%d"
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis,
        yearRange = IntRange(yearRange[0], yearRange[1])
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            calendar.timeInMillis = millis
                            val year = calendar.get(Calendar.YEAR)
                            val month = calendar.get(Calendar.MONTH)
                            val day = calendar.get(Calendar.DAY_OF_MONTH)
                            onDateSelected(year, month, day)
                        }
                    },
                    modifier = Modifier.padding(end = 6.dp)
                ) { Text(text = "OK") }
            },
            dismissButton = { 
                TextButton(onClick = { showDialog = false }) {
                    Text(text = stringResource(id = R.string.dismiss))
                }
            }
        ) { DatePicker(state = datePickerState) }
    }
    val selectedDateString = String.format(
        Locale.getDefault(),
        dateFormat,
        selectedDate.get(Calendar.DAY_OF_MONTH),
        selectedDate.get(Calendar.MONTH),
        selectedDate.get(Calendar.YEAR)
    )

    CompositionLocalProvider(value = LocalMinimumInteractiveComponentEnforcement provides false) {
        Text(
            text = selectedDateString,
            modifier = modifier.clickable { showDialog = true },
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.titleMedium,
            textDecoration = TextDecoration.Underline
        )
    }
}