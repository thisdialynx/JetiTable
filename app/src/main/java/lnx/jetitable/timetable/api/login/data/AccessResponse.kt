package lnx.jetitable.timetable.api.login.data

data class AccessResponse(
    val access: List<Int>,
    val accessToken: String,
    val status: String,
    val user: User
)

data class User(
    val fullName: String = "",
    val userId: Int = 0,
    val status: String = "",
    val fullNameId: Int = 0,
    val key: String = "",
    val group: String = "",
    val groupId: String = "",
    val isFullTime: Int = 0,
    val facultyCode: Int = 0
)
