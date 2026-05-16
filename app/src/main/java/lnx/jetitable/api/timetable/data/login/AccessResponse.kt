package lnx.jetitable.api.timetable.data.login

import lnx.jetitable.api.timetable.domain.models.User

data class AccessResponse(
    val access: List<Int>,
    val accessToken: String,
    val status: String,
    val user: User
)
