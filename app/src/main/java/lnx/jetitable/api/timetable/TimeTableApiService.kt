package lnx.jetitable.api.timetable

import lnx.jetitable.api.timetable.data.login.AccessRequest
import lnx.jetitable.api.timetable.data.login.AccessResponse
import lnx.jetitable.api.timetable.data.login.LoginRequest
import lnx.jetitable.api.timetable.data.login.LoginResponse
import lnx.jetitable.api.timetable.data.login.MailRequest
import lnx.jetitable.api.timetable.data.login.MailResponse
import lnx.jetitable.api.timetable.data.query.AttendanceData
import lnx.jetitable.api.timetable.data.query.AttendanceListRequest
import lnx.jetitable.api.timetable.data.query.ClassListRequest
import lnx.jetitable.api.timetable.data.query.ClassNetworkData
import lnx.jetitable.api.timetable.data.query.ExamListRequest
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.api.timetable.data.query.VerifyPresenceRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TimeTableApiService {

    @POST(AUTHORISATION_PHP)
    suspend fun checkPassword(
        @Header("Authorization") token: String,
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST(AUTHORISATION_PHP)
    suspend fun sendMail(
        @Body request: MailRequest
    ): Response<MailResponse>

    @POST(AUTHORISATION_PHP)
    suspend fun checkAccess(
        @Body request: AccessRequest
    ): Response<HtmlConverterState<AccessResponse>>

    @POST(QUERY_PHP)
    suspend fun get_listLessonTodayStudent(
        @Body request: ClassListRequest
    ): Response<HtmlConverterState<List<ClassNetworkData>>>

    @POST(QUERY_PHP)
    suspend fun get_checkZoom(
        @Body request: VerifyPresenceRequest
    ): Response<*>

    @POST(QUERY_PHP)
    suspend fun get_sessionStudent(
        @Body request: ExamListRequest
    ): Response<HtmlConverterState<List<ExamNetworkData>>>

    @POST(QUERY_PHP)
    suspend fun get_listStudent(
        @Body request: AttendanceListRequest
    ): Response<HtmlConverterState<List<AttendanceData>>>

    companion object {
        const val BASE_URL = "https://placeholder.com"

        // Endpoints
        const val AUTHORISATION_PHP = "placeholder"
        const val QUERY_PHP = "placeholder"

        // Avtorization.php methods
        const val CHECK_PASSWORD = "placeholder"
        const val PASSWORD_RECOVERY = "placeholder"
        const val CHECK_ACCESS = "placeholder"

        // getQuery.php methods and parameters
        const val DAILY_CLASS_LIST = "placeholder"
        const val STATE = "placeholder"
        const val PRESENCE_VERIFICATION = "placeholder"
        const val EXAM_LIST = "placeholder"
        const val ATTENDANCE_LIST = "placeholder"
    }
}