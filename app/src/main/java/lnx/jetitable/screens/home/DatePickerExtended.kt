package lnx.jetitable.screens.home

import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.misc.getAcademicYear
import lnx.jetitable.misc.getFormattedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerExtended(selectedDate: Calendar, modifier: Modifier = Modifier, onDateSelected: (Int, Int, Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    val calendar = java.util.Calendar.getInstance()
    val currentYear = calendar.get(java.util.Calendar.YEAR)
    val currentMonth = calendar.get(java.util.Calendar.MONTH) + 1
    val academicYear = getAcademicYear(currentYear, currentMonth)
    val yearRange = academicYear.split("/").map { it.toInt() }
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
                            val year = calendar.get(java.util.Calendar.YEAR)
                            val month = calendar.get(java.util.Calendar.MONTH)
                            val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
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
    val selectedDateString = getFormattedDate(
        selectedDate.get(java.util.Calendar.DAY_OF_MONTH),
        selectedDate.get(java.util.Calendar.MONTH) + 1,
        selectedDate.get(java.util.Calendar.YEAR)
    )

    CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        Text(
            text = selectedDateString,
            modifier = modifier.clickable { showDialog = true },
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            textDecoration = TextDecoration.Underline
        )
    }
}