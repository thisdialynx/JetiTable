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
        const val BASE_URL = "https://placeholder.com"
    }

    @POST("placeholder")
    suspend fun checkPassword(
        @Header("Authorization") token: String,
        @Body request: LoginRequest
    ): LoginResponse

    @POST("placeholder")
    suspend fun sendMail(
        @Body request: MailRequest
    ): MailResponse
}