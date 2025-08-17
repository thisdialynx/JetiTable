package lnx.jetitable.api.timetable.data.query

import kotlinx.serialization.Serializable

@Serializable
data class ExamNetworkData(
    val date: String,
    val time: String,
    val number: String,
    val name: String,
    val educator: String,
    val url: String
)


