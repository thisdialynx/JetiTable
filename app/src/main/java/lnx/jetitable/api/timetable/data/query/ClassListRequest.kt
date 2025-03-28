package lnx.jetitable.api.timetable.data.query

data class ClassListRequest(
    val param: String,
    val group: String,
    val id_group: String,
    val date: String,
    val year: String,
    val semestr: String
)
