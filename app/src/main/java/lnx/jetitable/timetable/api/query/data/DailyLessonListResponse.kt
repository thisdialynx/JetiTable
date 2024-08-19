package lnx.jetitable.timetable.api.query.data

data class DailyLessonListResponse(
    val lessons: List<Lesson>
)

data class Lesson(
    val idLesson: String,
    val group: String,
    val numLesson: String,
    val fio: String, // Teacher full name
    val lesson: String,
    val idFio: String, // Teacher id
    val dateLes: String, // Lesson date
    val timeBeg: String, // Lesson start time
    val timeEnd: String, // Lesson end time
    val items: String,
    val loadZoom: String, // Online meeting link
    val loadMoodleStudent: String, // Moodle link for lesson
    val type: String
)