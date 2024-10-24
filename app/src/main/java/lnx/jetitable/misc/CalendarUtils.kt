package lnx.jetitable.misc

import lnx.jetitable.timetable.api.query.data.Lesson
import java.util.Calendar
import java.util.Locale

const val dateFormat = "%02d.%02d.%d"
const val timeFormat = "%02d:%02d"

val calendar = Calendar.getInstance()
var currentMonth = calendar.get(Calendar.MONTH)
var currentYear = calendar.get(Calendar.YEAR)
var currentDay = calendar.get(Calendar.DAY_OF_MONTH)

fun getAcademicYear(year: Int = currentYear, month: Int = currentMonth): String {
    return if (month >= 8) "${year}/${year + 1}" else "${year - 1}/${year}"
}

fun getSemester(month: Int = currentMonth): Int {
    return if (month in 8..12 || month in 1..2) 1 else 2
}

fun getFormattedDate(day: Int = currentDay, month: Int = currentMonth, year: Int = currentYear): String {
    return String.format(Locale.getDefault(), dateFormat, day, month, year)
}

fun isLessonNow(lesson: Lesson, currentTime: Long): Boolean {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentTime

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val formattedTime = String.format(Locale.getDefault(), timeFormat, hour, minute)
    val formattedDate = getFormattedDate(
        calendar.get(Calendar.DAY_OF_MONTH),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.YEAR)
    )

    return formattedDate == lesson.dateLes && formattedTime >= lesson.timeBeg && formattedTime <= lesson.timeEnd
}