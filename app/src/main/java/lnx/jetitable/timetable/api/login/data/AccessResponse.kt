package lnx.jetitable.timetable.api.login.data

data class AccessResponse(
    val status: String,
    val access: String,
    val user: String
)

data class User(
    val fio: String,
    val id_user: String,
    val status: String,
    val id_fio: String,
    val group: String,
    val id_group: String,
    val denne: Int
)
