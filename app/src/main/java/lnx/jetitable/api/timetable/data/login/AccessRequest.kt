package lnx.jetitable.api.timetable.data.login

data class AccessRequest(
    val method: String,
    val semestr: String,
    val year: String
)
