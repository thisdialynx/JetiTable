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
fun DatePickerView(selectedDate: Calendar, modifier: Modifier = Modifier, onDateSelected: (Calendar) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    val academicYearRange = getAcademicYear(selectedDate).split("/").map { it.toInt() }
    val yearRange = if (getSemester(selectedDate) == 1) academicYearRange[0] else academicYearRange[1]
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.timeInMillis,
        yearRange = IntRange(yearRange, yearRange)
    )
    val selectedDateString = getFormattedDate(selectedDate)

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
                TextButton(onClick = { showDialog = false }) {
                    Text(text = stringResource(id = R.string.dismiss))
                }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Text(text = stringResource(id = R.string.schedule_for_day))
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