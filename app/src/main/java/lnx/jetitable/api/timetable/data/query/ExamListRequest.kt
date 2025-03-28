package lnx.jetitable.api.timetable.data.query

data class ExamListRequest(
    val param: String,
    val group: String,
    val id_group: String,
    val year: String,
    val semestr: String
)
