package lnx.jetitable.api.timetable.data.query

data class AttendanceListRequest(
    val param: String,
    val group: String,
    val numLesson: String,
    val lesson: String,
    val id_lesson: String,
    val type: String,
    val akademGroup: String,
    val date: String,
    val id_fio: String
)
