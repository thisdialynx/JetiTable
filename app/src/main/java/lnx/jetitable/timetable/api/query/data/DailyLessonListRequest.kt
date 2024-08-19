package lnx.jetitable.timetable.api.query.data

data class DailyLessonListRequest(
    val param: String,
    val group: String,
    val id_group: String,
    val date: String,
    val year: String,
    val semestr: String
)
