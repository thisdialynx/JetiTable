package lnx.jetitable.api.timetable

import android.util.Log
import lnx.jetitable.BuildConfig
import lnx.jetitable.api.timetable.data.login.AccessResponse
import lnx.jetitable.api.timetable.data.login.User
import lnx.jetitable.api.timetable.data.query.ClassNetworkData
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import okhttp3.ResponseBody
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.regex.Pattern

class HtmlConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation?>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (type == AccessResponse::class.java) {
            return Converter<ResponseBody, AccessResponse> {
                parseAccessResponse(it.string())
            }
        }

        val rawType = getRawType(type)
        if (rawType == List::class.java && type is ParameterizedType) {
            val parameterType = getParameterUpperBound(0, type)

            when (parameterType) {
                ExamNetworkData::class.java -> {
                    return Converter<ResponseBody, List<ExamNetworkData>> {
                        parseExamsHtml(it.string())
                    }
                }
                ClassNetworkData::class.java -> {
                    return Converter<ResponseBody, List<ClassNetworkData>> {
                        parseLessonHtml(it.string())
                    }
                }
            }
        }

        return null
    }
}

fun parseExamsHtml(html: String): List<ExamNetworkData> {
    val document: Document = Jsoup.parse("<html><body><table>$html</table></body></html>")
    val exams = mutableListOf<ExamNetworkData>()

    try {
        document.select("tr").forEach { row: Element ->
            val date = row.attr("data-date")
            val classNumber = row.attr("data-numlesson")
            val time = row.attr("data-time")
            val className = row.attr("data-lesson")
            val educator = row.attr("data-fioteacher")

            val url = row.select("span[onclick]").attr("onclick")
                .substringAfter("('").substringBefore("')")

            val examNetworkData = ExamNetworkData(
                date = date,
                time = time,
                number = classNumber,
                name = className,
                educator = educator,
                url = url
            )

            if (BuildConfig.DEBUG) Log.d("Session html parser", "Extracted data: $examNetworkData")

            exams.add(examNetworkData)
        }
    } catch (e: Exception) {
        Log.e("Session html parser", "Failed to parse html data", e)
    }
    return exams
}

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
        fullName = userJsonObject.getString("fio"),
        userId = userJsonObject.getInt("id_user"),
        status = userJsonObject.getString("status"),
        fullNameId = userJsonObject.getInt("id_fio"),
        key = userJsonObject.getString("key"),
        group = userJsonObject.getString("group"),
        groupId = userJsonObject.getString("id_group"),
        isFullTime = userJsonObject.getInt("denne"),
        facultyCode = userJsonObject.getInt("kod_faculty")
    )

    return AccessResponse(
        access = access,
        accessToken = accessToken,
        status = status,
        user = user
    )
}