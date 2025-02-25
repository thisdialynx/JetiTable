package lnx.jetitable.timetable.api.query.data

import android.util.Log
import lnx.jetitable.BuildConfig
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.regex.Pattern

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
    val meetingLink: String,
    val moodleLink: String,
    val type: String,
    val room: String
)

fun parseLessonHtml(html: String): List<ClassNetworkData> {
    val classes = mutableListOf<ClassNetworkData>()

    if (html.contains("відсутні", ignoreCase = true)) {
        return emptyList()
    }

    val doc: Document = Jsoup.parse("<html><body><table>$html</table></body></html>")
    val elements = doc.select("tr.tr1")

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
            val room = element.select("td.tabPSC").last()?.text() ?: ""

            val classNetworkData = ClassNetworkData(
                id = element.attr("data-id_lesson"),
                group = element.attr("data-group"),
                number = element.attr("data-numlesson"),
                educator = element.attr("data-fio"),
                name = element.attr("data-lesson"),
                educatorId = element.attr("data-id_fio"),
                date = element.attr("data-dateles"),
                start = element.attr("data-timebeg").substring(0, 5),
                end = element.attr("data-timeend").substring(0, 5),
                items = element.attr("data-items"),
                meetingLink = meetingLink,
                moodleLink = moodleLink,
                type = type,
                room = room
            )

            if (BuildConfig.DEBUG) Log.d("Lession html parser", "Extracted data: $classNetworkData")

            classes.add(classNetworkData)
        }
    } catch (e: Exception) {
        Log.e("Lesson html parser", "Failed to parse html response", e)
    }

    return classes
}