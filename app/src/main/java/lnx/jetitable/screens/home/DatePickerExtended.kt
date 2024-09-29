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
import lnx.jetitable.misc.getSemester

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerExtended(selectedDate: Calendar, modifier: Modifier = Modifier, onDateSelected: (Int, Int, Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    val currentMonth = selectedDate.get(Calendar.MONTH)
    val academicYear = getAcademicYear(selectedDate.get(Calendar.YEAR), currentMonth)
    val academicYearRange = academicYear.split("/").map { it.toInt() }
    val yearRange = if (getSemester(currentMonth) == 1) academicYearRange[0] else academicYearRange[1]
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis,
        yearRange = IntRange(yearRange, yearRange)
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate.timeInMillis = millis
                            onDateSelected(
                                selectedDate.get(Calendar.YEAR),
                                selectedDate.get(Calendar.MONTH),
                                selectedDate.get(Calendar.DAY_OF_MONTH)
                            )
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
        selectedDate.get(Calendar.DAY_OF_MONTH),
        selectedDate.get(Calendar.MONTH) + 1,
        selectedDate.get(Calendar.YEAR)
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