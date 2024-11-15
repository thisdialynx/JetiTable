package lnx.jetitable.timetable.api

import lnx.jetitable.timetable.api.login.data.AccessRequest
import lnx.jetitable.timetable.api.login.data.LoginRequest
import lnx.jetitable.timetable.api.login.data.LoginResponse
import lnx.jetitable.timetable.api.login.data.MailRequest
import lnx.jetitable.timetable.api.login.data.MailResponse
import lnx.jetitable.timetable.api.query.data.LessonListRequest
import lnx.jetitable.timetable.api.query.data.SessionListRequest
import lnx.jetitable.timetable.api.query.data.VerifyPresenceRequest
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
    ): String

    @POST(QUERY_PHP)
    suspend fun get_listLessonTodayStudent(
        @Body request: LessonListRequest
    ): String

    @POST(QUERY_PHP)
    suspend fun get_checkZoom(
        @Body request: VerifyPresenceRequest
    ): String

    @POST(QUERY_PHP)
    suspend fun get_sessionStudent(
        @Body request: SessionListRequest
    ): String

    companion object {
        const val BASE_URL = "https://timetable.lond.lg.ua"

        // Endpoints
        const val AUTHORISATION_PHP = "php/Avtorization.php"
        const val QUERY_PHP = "php/getQuery.php"

        // Avtorization.php methods
        const val CHECK_PASSWORD = "checkPassword"
        const val SEND_MAIL = "sendMail"
        const val CHECK_ACCESS = "checkAccess"

        // getQuery.php methods and parameters
        const val DAILY_LESSON_LIST = "get_listLessonTodayStudent"
        const val STATE = "s"
        const val CHECK_ZOOM = "get_checkZoom"
        const val SESSION_LIST = "get_sessionStudent"
    }
}