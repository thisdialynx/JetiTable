package lnx.jetitable.features.auth.data.repository

import android.util.Log
import lnx.jetitable.api.timetable.HtmlConverterState
import lnx.jetitable.api.timetable.TimeTableApiService
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.CHECK_ACCESS
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.CHECK_PASSWORD
import lnx.jetitable.api.timetable.data.login.AccessRequest
import lnx.jetitable.api.timetable.data.login.LoginRequest
import lnx.jetitable.api.timetable.domain.models.SemesterType
import lnx.jetitable.features.auth.domain.model.AuthError
import lnx.jetitable.features.auth.domain.model.AuthResult
import lnx.jetitable.features.auth.domain.repository.AuthRepository
import lnx.jetitable.misc.DateHelper
import okhttp3.Credentials
import okio.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: TimeTableApiService,
    private val dateHelper: DateHelper,
) : AuthRepository {

    override suspend fun login(login: String, password: String): AuthResult {
        return try {
            val basicAuth = Credentials.basic(login, password)
            val passResponse = api.checkPassword(
                basicAuth,
                LoginRequest(CHECK_PASSWORD, login, password)
            )

            if (passResponse.isSuccessful) {
                val semester = if (dateHelper.getSemester() == SemesterType.AUTUMN) 1 else 2
                Log.d("Semester Int", "$semester")
                val year = dateHelper.getAcademicYears()

                val accessResponse = api.checkAccess(
                    AccessRequest(CHECK_ACCESS, semester.toString(), year)
                )

                if (accessResponse.isSuccessful) {
                    when (val body = accessResponse.body()) {
                        is HtmlConverterState.Success -> {
                            val data = body.data
                            AuthResult.Success(data.user)
                        }

                        HtmlConverterState.Empty -> {
                            AuthResult.Failure(AuthError.EMPTY_RESPONSE)
                        }

                        else -> {
                            AuthResult.Failure(AuthError.UNKNOWN_ERROR)
                        }
                    }
                } else {
                    AuthResult.Failure(AuthError.NETWORK_ERROR)
                }
            } else {
                AuthResult.Failure(AuthError.INVALID_CREDENTIALS)
            }
        } catch (e: IOException) {
            AuthResult.Failure(AuthError.NETWORK_ERROR)
        } catch (e: Exception) {
            AuthResult.Failure(AuthError.UNKNOWN_ERROR)
        }
    }
}