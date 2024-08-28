package lnx.jetitable.screens.home

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import lnx.jetitable.misc.getAcademicYear
import lnx.jetitable.misc.getCurrentDate
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerExtended(context: Context, onDateSelected: (Int, Int, Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH) + 1
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
                            calendar.apply { timeInMillis = millis }
                            onDateSelected(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            )
                        }
                    }
                ) {
                    Text(text = "OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    Text(
        text = datePickerState.selectedDateMillis?.let { millis ->
            val formatter = remember { DatePickerDefaults.dateFormatter() }
            val locale = Locale.getDefault()
            formatter.formatDate(millis, locale)
        } ?: getCurrentDate(),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.clickable { showDialog = true }
    )
}