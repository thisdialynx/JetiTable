package lnx.jetitable.misc

import lnx.jetitable.timetable.api.query.data.Lesson
import java.util.Calendar
import java.util.Locale

const val dateFormat = "%02d.%02d.%d"
const val timeFormat = "%02d:%02d"

fun getAcademicYear(year: Int, month: Int): String {
    return if (month >= 8) "${year}/${year + 1}" else "${year - 1}/${year}"
}

fun getSemester(month: Int): Int {
    return if (month in 8..12 || month in 1..2) 1 else 2
}

fun getFormattedDate(day: Int, month: Int, year: Int): String {
    return String.format(Locale.getDefault(), dateFormat, day, month, year)
}