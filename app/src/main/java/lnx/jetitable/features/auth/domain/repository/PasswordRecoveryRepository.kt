package lnx.jetitable.features.auth.domain.repository

import lnx.jetitable.features.auth.domain.model.RecoveryResult

interface PasswordRecoveryRepository {
    suspend fun sendRecoveryEmail(login: String): RecoveryResult
}