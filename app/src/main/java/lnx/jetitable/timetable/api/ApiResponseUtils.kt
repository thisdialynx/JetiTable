package lnx.jetitable.timetable.api

import android.util.Log
import com.google.gson.Gson
import lnx.jetitable.BuildConfig
import lnx.jetitable.timetable.api.login.data.User
import lnx.jetitable.timetable.api.query.data.DailyLessonListResponse
import lnx.jetitable.timetable.api.query.data.Lesson
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.regex.Pattern

fun parseUserJson(userJson: String): User {
    return Gson().fromJson(userJson, User::class.java)
}

fun dailyLessonListDataExtractor(response: String): DailyLessonListResponse {

    // Since the response does not contain html, body and table tags, we add them manually
    val correctResponse = "<html><body><table>$response</table></body></html>"

    val doc: Document = Jsoup.parse(correctResponse)
    val elements = doc.select("tr.tr1")
    val extractedData = mutableListOf<Lesson>()

    val meetingPattern = Pattern.compile("Modul_TT\\.loadZoom\\('([^']+)'\\)")
    val moodlePattern = Pattern.compile("Modul_TT\\.loadMoodleStudent\\('([^']+)'\\)")

    try {
        for (element in elements) {
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
                idLesson = element.attr("data-id_lesson"),
                group = element.attr("data-group"),
                numLesson = element.attr("data-numlesson"),
                fio = element.attr("data-fio"),
                lesson = element.attr("data-lesson"),
                idFio = element.attr("data-id_fio"),
                dateLes = element.attr("data-dateles"),
                timeBeg = element.attr("data-timebeg").substring(0, 5),
                timeEnd = element.attr("data-timeend").substring(0, 5),
                items = element.attr("data-items"),
                loadZoom = meetingLink,
                loadMoodleStudent = moodleLink,
                type = type
            )

            if (BuildConfig.DEBUG) {
                Log.d("API response utils", "Extracted data: $lesson")
            }

            extractedData.add(lesson)
        }

    } catch (e: NoSuchElementException) {
        Log.d("API response utils", "No data found", e)
    } catch (e: Exception) {
        Log.d("API response utils", "Error occurred", e)
    }

    return DailyLessonListResponse(lessons = extractedData)
}
