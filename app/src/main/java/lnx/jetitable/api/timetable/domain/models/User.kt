package lnx.jetitable.api.timetable.domain.models

data class User(
    val fullName: String,
    val userId: Int,
    val status: String,
    val fullNameId: Int,
    val key: String,
    val group: String,
    val groupId: String,
    val isFullTime: Int,
    val facultyCode: Int
)
