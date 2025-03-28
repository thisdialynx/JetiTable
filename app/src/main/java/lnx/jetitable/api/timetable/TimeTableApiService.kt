package lnx.jetitable.api.timetable

import lnx.jetitable.api.timetable.data.login.AccessRequest
import lnx.jetitable.api.timetable.data.login.AccessResponse
import lnx.jetitable.api.timetable.data.login.LoginRequest
import lnx.jetitable.api.timetable.data.login.LoginResponse
import lnx.jetitable.api.timetable.data.login.MailRequest
import lnx.jetitable.api.timetable.data.login.MailResponse
import lnx.jetitable.api.timetable.data.query.ClassListRequest
import lnx.jetitable.api.timetable.data.query.ClassNetworkData
import lnx.jetitable.api.timetable.data.query.ExamListRequest
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.api.timetable.data.query.VerifyPresenceRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TimeTableApiService {

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
        @Body request: ClassListRequest
    ): List<ClassNetworkData>

    @POST(QUERY_PHP)
    suspend fun get_checkZoom(
        @Body request: VerifyPresenceRequest
    ): String

    @POST(QUERY_PHP)
    suspend fun get_sessionStudent(
        @Body request: ExamListRequest
    ): List<ExamNetworkData>

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
        const val DAILY_CLASS_LIST = "get_listLessonTodayStudent"
        const val STATE = "s"
        const val CHECK_ZOOM = "get_checkZoom"
        const val EXAM_LIST = "get_sessionStudent"
    }
}