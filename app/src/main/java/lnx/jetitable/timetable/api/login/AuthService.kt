package lnx.jetitable.timetable.api.login

import lnx.jetitable.timetable.api.login.data.LoginRequest
import lnx.jetitable.timetable.api.login.data.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("placeholder")
    suspend fun checkPassword(
        @Header("placeholder") token: String,
        @Body request: LoginRequest
    ): LoginResponse
}