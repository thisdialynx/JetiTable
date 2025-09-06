package lnx.jetitable.screens.home.elements.datepicker

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
data class DateState(
    val formattedDate: String = "",
    val datePickerState: DatePickerState = DatePickerState(
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis,
        yearRange = DatePickerDefaults.YearRange,
        locale = Locale.ROOT
    )
)