package lnx.jetitable.timetable.api.login.data

data class AccessRequest(
    val method: String,
    val semestr: String,
    val year: String
)
