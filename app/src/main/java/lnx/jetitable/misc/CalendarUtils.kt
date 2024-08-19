package lnx.jetitable.misc

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()
    return dateFormat.format(calendar.time)
}

fun getAcademicYear(currentYear: Int, currentMonth: Int): String {
    return if (currentMonth in 1..7) {
        "${currentYear - 1}/$currentYear"
    } else {
        "$currentYear/${currentYear + 1}"
    }
}

fun getSemester(currentMonth: Int): String {
    return if (currentMonth in 1..7) "2" else "1"
}