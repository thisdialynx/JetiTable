package lnx.jetitable.timetable.api.query.data

import android.util.Log
import lnx.jetitable.BuildConfig
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

data class SessionListResponse(
    val sessions: List<Session>
)

data class Session(
    val date: String,
    val lessonTime: String,
    val lessonNumber: String,
    val lessonName: String,
    val teacher: String,
    val url: String
)

fun parseSessionHtml(html: String): SessionListResponse {
    val document: Document = Jsoup.parse("<html><body><table>$html</table></body></html>")
    val sessions = mutableListOf<Session>()

    try {
        document.select("tr").forEach { row: Element ->
            val date = row.attr("data-date")
            val lessonNumber = row.attr("data-numlesson")
            val time = row.attr("data-time")
            val lessonName = row.attr("data-lesson")
            val teacher = row.attr("data-fioteacher")

            val url = row.select("span[onclick]").attr("onclick")
                .substringAfter("('").substringBefore("')")

            val session = Session(
                date = date,
                lessonTime = time,
                lessonNumber = lessonNumber,
                lessonName = lessonName,
                teacher = teacher,
                url = url
            )

            if (BuildConfig.DEBUG) Log.d("Session html parser", "Extracted data: $session")

            sessions.add(session)
        }
    } catch (e: Exception) {
        Log.e("Session html parser", "Failed to parse html data", e)
    }
    return SessionListResponse(sessions)
}
