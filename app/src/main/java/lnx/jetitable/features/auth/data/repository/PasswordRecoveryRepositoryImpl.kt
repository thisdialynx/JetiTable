package lnx.jetitable.features.auth.data.repository

import lnx.jetitable.api.timetable.TimeTableApiService
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.PASSWORD_RECOVERY
import lnx.jetitable.api.timetable.data.login.MailRequest
import lnx.jetitable.features.auth.domain.model.RecoveryError
import lnx.jetitable.features.auth.domain.model.RecoveryResult
import lnx.jetitable.features.auth.domain.repository.PasswordRecoveryRepository
import okio.IOException
import timber.log.Timber
import javax.inject.Inject

class PasswordRecoveryRepositoryImpl @Inject constructor(
    private val api: TimeTableApiService
) : PasswordRecoveryRepository {

    override suspend fun sendRecoveryEmail(login: String): RecoveryResult {
        return try {
            val response = api.sendMail(
                MailRequest(PASSWORD_RECOVERY, login)
            )

            if (response.isSuccessful) RecoveryResult.Success
            else RecoveryResult.Failure(RecoveryError.INVALID_CREDENTIALS)
        } catch (e: IOException) {
            Timber.e(e)
            RecoveryResult.Failure(RecoveryError.NETWORK_ERROR)
        } catch (e: Exception) {
            Timber.e(e)
            RecoveryResult.Failure(RecoveryError.UNKNOWN_ERROR)
        }
    }
}