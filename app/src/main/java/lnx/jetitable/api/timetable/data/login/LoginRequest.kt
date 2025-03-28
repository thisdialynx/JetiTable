package lnx.jetitable.api.timetable.data.login

data class LoginRequest(
    val method: String,
    val login: String,
    val password: String
)
