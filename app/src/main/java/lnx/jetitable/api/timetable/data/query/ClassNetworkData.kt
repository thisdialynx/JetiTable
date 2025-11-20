package lnx.jetitable.api.timetable.data.query

import kotlinx.serialization.Serializable

@Serializable
data class ClassNetworkData(
    val id: String,
    val group: String,
    val number: String,
    val educator: String,
    val name: String,
    val educatorId: String,
    val date: String,
    val start: String,
    val end: String,
    val items: String,
    val weeks: List<String> = emptyList(),
    val meetingLink: String,
    val moodleLink: String,
    val type: String,
    val room: String
)