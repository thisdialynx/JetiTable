package lnx.jetitable.timetable.api.login.data

data class LoginResponse(
    val status: String,
    val token: String,
    val message: String? = null
)
