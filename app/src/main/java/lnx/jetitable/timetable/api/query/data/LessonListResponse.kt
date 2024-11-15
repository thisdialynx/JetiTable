package lnx.jetitable.timetable.api.query.data

import android.util.Log
import lnx.jetitable.BuildConfig
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.regex.Pattern

data class LessonListResponse(
    val lessons: List<Lesson>
)

data class Lesson(
    val id: String,
    val group: String,
    val number: String,
    val teacherFullName: String,
    val name: String,
    val teacherId: String,
    val date: String,
    val start: String,
    val end: String,
    val items: String, // Contains nothing every time. We don't know what is it used for
    val meetingLink: String,
    val moodleLink: String,
    val type: String
)

fun parseLessonHtml(html: String): LessonListResponse {

    val doc: Document = Jsoup.parse("<html><body><table>$html</table></body></html>")
    val elements = doc.select("tr.tr1")
    val extractedData = mutableListOf<Lesson>()

    val meetingPattern = Pattern.compile("Modul_TT\\.loadZoom\\('([^']+)'\\)")
    val moodlePattern = Pattern.compile("Modul_TT\\.loadMoodleStudent\\('([^']+)'\\)")

    try {
        elements.forEach { element ->
            val meetingButtonElement: Element? = element.selectFirst("button[onclick^=Modul_TT.loadZoom]")
            val moodleButtonElement: Element? = element.selectFirst("button[onclick^=Modul_TT.loadMoodleStudent]")

            val meetingOnclickAttr: String? = meetingButtonElement?.attr("onclick")
            val moodleOnclickAttr: String? = moodleButtonElement?.attr("onclick")

            val meetingMatcher = meetingPattern.matcher(meetingOnclickAttr ?: "")
            val moodleMatcher = moodlePattern.matcher(moodleOnclickAttr ?: "")

            val meetingLink = if (meetingMatcher.find()) meetingMatcher.group(1) else ""
            val moodleLink = if (moodleMatcher.find()) moodleMatcher.group(1) else ""

            val type = element.selectFirst("td.tabPSC[style*=text-align:center; font-weight:bold;]")?.text() ?: ""

            val lesson = Lesson(
                id = element.attr("data-id_lesson"),
                group = element.attr("data-group"),
                number = element.attr("data-numlesson"),
                teacherFullName = element.attr("data-fio"),
                name = element.attr("data-lesson"),
                teacherId = element.attr("data-id_fio"),
                date = element.attr("data-dateles"),
                start = element.attr("data-timebeg").substring(0, 5),
                end = element.attr("data-timeend").substring(0, 5),
                items = element.attr("data-items"),
                meetingLink = meetingLink,
                moodleLink = moodleLink,
                type = type
            )

            if (BuildConfig.DEBUG) Log.d("Lession html parser", "Extracted data: $lesson")

            extractedData.add(lesson)
        }
    } catch (e: Exception) {
        Log.e("Lesson html parser", "Failed to parse html response", e)
    }

    return LessonListResponse(lessons = extractedData)
}