package lnx.jetitable.features.home.presentation

import lnx.jetitable.api.timetable.data.query.ClassNetworkData

data class ClassUiData(
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
    val weeks: List<String>,
    val meetingLink: String,
    val moodleLink: String,
    val type: String,
    val room: String,
    val isNow: Boolean
)

fun ClassNetworkData.toUi(currentDate: String, currentTime: String): ClassUiData {
    val isCurrentDate = currentDate == this.date
    val isTimeInRange = currentTime in this.start..this.end
    val isNow = isCurrentDate && isTimeInRange

    return ClassUiData(
        id,
        group,
        number,
        educator,
        name,
        educatorId,
        date,
        start,
        end,
        items,
        weeks,
        meetingLink,
        moodleLink,
        type,
        room,
        isNow
    )
}