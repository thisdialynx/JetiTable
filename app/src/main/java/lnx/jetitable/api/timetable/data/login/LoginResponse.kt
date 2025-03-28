package lnx.jetitable.api.timetable.data.login

data class LoginResponse(
    val status: String,
    val token: String,
    val message: String? = null
)
