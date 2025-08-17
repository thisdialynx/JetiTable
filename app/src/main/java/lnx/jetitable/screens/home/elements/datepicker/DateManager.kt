package lnx.jetitable.screens.home.elements.datepicker

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
class DateManager {
    private val _selectedDate = MutableStateFlow(Calendar.getInstance())
    val selectedDate = _selectedDate.asStateFlow()
    private val locale = Locale.getDefault()
    val dateFormat = SimpleDateFormat(DATE_FORMAT, locale)
    val timeFormat = SimpleDateFormat(TIME_FORMAT, locale)

    private val selectedYear = selectedDate.value.get(Calendar.YEAR)
    private val selectedMonth = selectedDate.value.get(Calendar.MONTH)
    private val academicYearsInt = getAcademicYears().split("/").map { it.toInt() }
    private val currentAcademicYear = if (getSemester() == 1) academicYearsInt[0]..academicYearsInt[0] else academicYearsInt[1]..academicYearsInt[1]

    val dateStateFlow = selectedDate.map {
        DateState(
            formattedDate = getFormattedDate(),
            datePickerState = DatePickerState(
                initialSelectedDateMillis = it.timeInMillis,
                yearRange = currentAcademicYear,
                locale = Locale("ROOT")
            )
        )
    }

    fun updateDate(calendar: Calendar = _selectedDate.value, dayShift: Int = 0) {
        val newDate = (calendar.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, dayShift)
        }

        if (newDate.get(Calendar.YEAR) == currentAcademicYear.first) {
            _selectedDate.update { newDate }
        } else {
            _selectedDate.update { (calendar.clone() as Calendar).apply { set(Calendar.YEAR, currentAcademicYear.first) } }
        }
    }

    fun getAcademicYears(): String {
        return if (selectedMonth >= 8) {
            "${selectedYear}/${selectedYear + 1}"
        } else {
            "${selectedYear - 1}/${selectedYear}"
        }
    }

    fun getSemester(): Int {
        return if (selectedMonth in 8..12) 1 else 2
    }

    fun getFormattedDate(): String {
        val date = dateFormat.format(selectedDate.value.time)
        return date
    }

    companion object {
        const val DATE_FORMAT = "dd.MM.yyyy"
        const val TIME_FORMAT = "HH:mm"
    }
}