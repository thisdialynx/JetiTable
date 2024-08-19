package lnx.jetitable.timetable.api

import lnx.jetitable.timetable.api.login.data.LoginRequest
import lnx.jetitable.timetable.api.login.data.LoginResponse
import lnx.jetitable.timetable.api.login.data.MailRequest
import lnx.jetitable.timetable.api.login.data.MailResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST(AUTHORISATION_PHP)
    suspend fun checkPassword(
        @Header("Authorization") token: String,
        @Body request: LoginRequest
    ): LoginResponse

    @POST(AUTHORISATION_PHP)
    suspend fun sendMail(
        @Body request: MailRequest
    ): MailResponse

    @POST(AUTHORISATION_PHP)
    suspend fun checkAccess(
        @Body request: AccessRequest
    ): AccessResponse

    @POST(QUERY_PHP)
    suspend fun get_listLessonTodayStudent(
        @Body request: DailyLessonListRequest
    ): String

    companion object {
        const val BASE_URL = "https://placeholder.com"

        // Endpoints
        const val AUTHORISATION_PHP = "placeholder"
        const val QUERY_PHP = "placeholder"

        // Avtorization.php methods
        const val CHECK_PASSWORD = "placeholder"
        const val SEND_MAIL = "placeholder"
        const val CHECK_ACCESS = "placeholder"

        // getQuery.php methods
        const val DAILY_LESSON_LIST = "placeholder"
    }
}