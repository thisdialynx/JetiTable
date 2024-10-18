package lnx.jetitable.timetable.api.login.data

data class AccessResponse(
    val access: List<Int>,
    val accessToken: String,
    val status: String,
    val user: User
)

data class User(
    val fio: String,
    val id_user: Int,
    val status: String,
    val id_fio: Int,
    val key: String,
    val group: String,
    val id_group: String,
    val denne: Int,
    val kod_faculty: Int
)
