package lnx.jetitable.core

data class DaySchedule(
    val day: String,
    val week: Int,
    val lessons: List<Lesson>
)

data class Lesson(
    val time: String,
    val subject: String,
    val teacher: String,
    val meetingApp: String?,
    val room: String?,
    val lessionType: String,
    val studentGroup: String
)
