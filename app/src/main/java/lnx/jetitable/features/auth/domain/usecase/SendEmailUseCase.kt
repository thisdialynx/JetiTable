package lnx.jetitable.features.auth.domain.usecase

import lnx.jetitable.features.auth.domain.model.RecoveryError
import lnx.jetitable.features.auth.domain.model.RecoveryResult
import lnx.jetitable.features.auth.domain.repository.PasswordRecoveryRepository
import javax.inject.Inject

class SendEmailUseCase @Inject constructor(
    private val pwRecoveryRepository: PasswordRecoveryRepository
) {
    suspend operator fun invoke(login: String): RecoveryResult {
        return if (login.endsWith("@snu.edu.ua")) {
            pwRecoveryRepository.sendRecoveryEmail(login)
        } else RecoveryResult.Failure(RecoveryError.NOT_CORPORATE_EMAIL)
    }
}