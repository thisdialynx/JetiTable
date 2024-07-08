package lnx.jetitable.timetable.data

data class Schedule(
    val date: String,
    val week: Int,
    val subjects: List<Subject>
)

// To be replaced with real data
val DailySchedules = listOf<Schedule>(
    Schedule(
        date = "07.06.2024",
        week = 17,
        subjects = Subjects
    )
)