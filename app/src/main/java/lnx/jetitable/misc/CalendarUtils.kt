package lnx.jetitable.misc

import lnx.jetitable.timetable.api.query.data.Lesson
import android.icu.util.Calendar
import java.util.Locale

const val dateFormat = "%02d.%02d.%d"
const val timeFormat = "%02d:%02d"

val calendar: Calendar = Calendar.getInstance()

fun getAcademicYear(selectedDate: Calendar = calendar): String {
    return if (selectedDate.get(Calendar.MONTH) >= 8) {
        "${selectedDate.get(Calendar.YEAR)}/${selectedDate.get(Calendar.YEAR) + 1}"
    } else {
        "${selectedDate.get(Calendar.YEAR) - 1}/${selectedDate.get(Calendar.YEAR)}"
    }
}

fun getSemester(selectedDate: Calendar = calendar): Int {
    return if (selectedDate.get(Calendar.MONTH) in 8..12 || selectedDate.get(Calendar.MONTH) in 1..2) 1 else 2
}

fun getFormattedDate(currentDate: Calendar = calendar): String {
    return String.format(
        Locale.getDefault(),
        dateFormat,
        currentDate.get(Calendar.DAY_OF_MONTH),
        currentDate.get(Calendar.MONTH) + 1,
        currentDate.get(Calendar.YEAR)
    )
}

fun isLessonNow(lesson: Lesson, currentTime: Long): Boolean {
    val currentDate = Calendar.getInstance()
    currentDate.timeInMillis = currentTime

    val hour = currentDate.get(Calendar.HOUR_OF_DAY)
    val minute = currentDate.get(Calendar.MINUTE)

    val formattedTime = String.format(Locale.getDefault(), timeFormat, hour, minute)
    val formattedDate = getFormattedDate(currentDate)

    return formattedDate == lesson.date && formattedTime >= lesson.start && formattedTime <= lesson.end
}