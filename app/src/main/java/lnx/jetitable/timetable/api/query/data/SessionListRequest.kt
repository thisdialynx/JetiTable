package lnx.jetitable.timetable.api.query.data

data class SessionListRequest(
    val param: String,
    val group: String,
    val id_group: String,
    val year: String,
    val semestr: String
)
