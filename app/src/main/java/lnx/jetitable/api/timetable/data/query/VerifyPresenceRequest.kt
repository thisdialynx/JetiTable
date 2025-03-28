package lnx.jetitable.api.timetable.data.query

data class VerifyPresenceRequest(
    val param: String,
    val state: String,
    val group: String,
    val fioUser: String,
    val idUser: String,
    val numLesson: String,
    val lesson: String,
    val id_lesson: String,
    val type: String,
    val fioTeacher: String,
    val id_fio: String,
    val dateles: String,
    val timebeg: String,
    val timeend: String,
)
