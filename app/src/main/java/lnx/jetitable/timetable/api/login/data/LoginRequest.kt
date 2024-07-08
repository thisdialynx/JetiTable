package lnx.jetitable.timetable.api.login.data

data class LoginRequest(
    val method: String,
    val login: String,
    val password: String
)
