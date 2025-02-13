package lnx.jetitable.timetable.api.query.data

import android.util.Log
import lnx.jetitable.BuildConfig
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

data class Exam(
    val date: String,
    val time: String,
    val number: String,
    val name: String,
    val educator: String,
    val url: String
)

fun parseSessionHtml(html: String): List<Exam> {
    val document: Document = Jsoup.parse("<html><body><table>$html</table></body></html>")
    val exams = mutableListOf<Exam>()

    try {
        document.select("tr").forEach { row: Element ->
            val date = row.attr("data-date")
            val classNumber = row.attr("data-numlesson")
            val time = row.attr("data-time")
            val className = row.attr("data-lesson")
            val educator = row.attr("data-fioteacher")

            val url = row.select("span[onclick]").attr("onclick")
                .substringAfter("('").substringBefore("')")

            val exam = Exam(
                date = date,
                time = time,
                number = classNumber,
                name = className,
                educator = educator,
                url = url
            )

            if (BuildConfig.DEBUG) Log.d("Session html parser", "Extracted data: $exam")

            exams.add(exam)
        }
    } catch (e: Exception) {
        Log.e("Session html parser", "Failed to parse html data", e)
    }
    return exams
}
