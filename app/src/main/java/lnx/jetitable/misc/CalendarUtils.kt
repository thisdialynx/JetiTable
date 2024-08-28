package lnx.jetitable.misc

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()
    return dateFormat.format(calendar.time)
}

fun getAcademicYear(year: Int, month: Int): String {
    return if (month >= 8) {
        "${year}/${year + 1}"
    } else {
        "${year - 1}/${year}"
    }
}

fun getSemester(month: Int): String {
    return if (month in 8..12 || month in 1..2) {
        "1"
    } else {
        "2"
    }
}