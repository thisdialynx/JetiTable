package lnx.jetitable.timetable.api

import android.util.Log
import lnx.jetitable.BuildConfig
import lnx.jetitable.timetable.api.login.data.AccessResponse
import lnx.jetitable.timetable.api.login.data.User
import lnx.jetitable.timetable.api.query.data.DailyLessonListResponse
import lnx.jetitable.timetable.api.query.data.Lesson
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.regex.Pattern

fun parseAccessResponse(jsonString: String): AccessResponse {
    val jsonObject = JSONObject(jsonString)

    val access = jsonObject.getJSONArray("access").let { array ->
        List(array.length()) { array.getInt(it)}
    }
    val accessToken = jsonObject.getString("accessToken")
    val status = jsonObject.getString("status")

    val userJsonString = jsonObject.getString("user")
    val userJsonObject = JSONObject(userJsonString)

    val user = User(
        fio = userJsonObject.getString("fio"),
        id_user = userJsonObject.getInt("id_user"),
        status = userJsonObject.getString("status"),
        id_fio = userJsonObject.getInt("id_fio"),
        key = userJsonObject.getString("key"),
        group = userJsonObject.getString("group"),
        id_group = userJsonObject.getString("id_group"),
        denne = userJsonObject.getInt("denne"),
        kod_faculty = userJsonObject.getInt("kod_faculty")
    )

    return AccessResponse(
        access = access,
        accessToken = accessToken,
        status = status,
        user = user
    )
}

fun parseLessonHtml(response: String): DailyLessonListResponse {

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
                Log.d("API response utils", "Extracted lesson data: $lesson")
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
