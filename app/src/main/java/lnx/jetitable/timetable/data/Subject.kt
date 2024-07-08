package lnx.jetitable.timetable.data

data class Subject(
    val startTime: String,
    val endTime: String,
    val name: String,
    val educator: String,
    val meetingApp: String,
    val room: String,
    val type: String,
    val studentGroup: String
)

// To be replaced with real data
val Subjects = listOf<Subject>(
    Subject(
        startTime = "9:30",
        endTime = "10:30",
        name = "Physical education",
        educator = "Sr. Ed. Kryvobohova N. P.",
        meetingApp = "Microsoft Teams",
        room = "",
        type = "PT",
        studentGroup = "IPZ-22d",
    ),
    Subject(
        startTime = "14:20",
        endTime = "15:20",
        name = "Programming for potaxies",
        educator = "Docent Ratov D. V.",
        meetingApp = "Zoom",
        room = "",
        type = "LT",
        studentGroup = "ITP-201d",
    ),
    Subject(
        startTime = "15:30",
        endTime = "16:30",
        name = "Programming for mobile platforms and something else",
        educator = "Docent Ratov D. V.",
        meetingApp = "Zoom",
        room = "",
        type = "LB",
        studentGroup = "ITP-201d",
    ),
    Subject(
        startTime = "16:40",
        endTime = "17:40",
        name = "Programming for mobile platforms",
        educator = "Docent Ratov D. V.",
        meetingApp = "Zoom",
        room = "",
        type = "LB",
        studentGroup = "ITP-201d",
    ),
)