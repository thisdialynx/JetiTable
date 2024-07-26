package lnx.jetitable.timetable.api

import lnx.jetitable.timetable.api.login.data.LoginRequest
import lnx.jetitable.timetable.api.login.data.LoginResponse
import lnx.jetitable.timetable.api.login.data.MailRequest
import lnx.jetitable.timetable.api.login.data.MailResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    companion object {
        const val BASE_URL = "https://timetable.lond.lg.ua"

        // BASE_URL endpoints
        const val AUTHORISATION_PHP = "php/Avtorization.php"

        // Avtorization.php methods
        const val CHECK_PASSWORD = "checkPassword"
        const val SEND_MAIL = "sendMail"
        const val CHECK_ACCESS = "checkAccess"
    }

    @POST(AUTHORISATION_PHP)
    suspend fun checkPassword(
        @Header("Authorization") token: String,
        @Body request: LoginRequest
    ): LoginResponse

    @POST(AUTHORISATION_PHP)
    suspend fun sendMail(
        @Body request: MailRequest
    ): MailResponse

}