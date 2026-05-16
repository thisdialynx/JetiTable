package lnx.jetitable.features.auth.domain.usecase

import lnx.jetitable.datastore.UserInfoStore
import lnx.jetitable.features.auth.domain.model.AuthError
import lnx.jetitable.features.auth.domain.model.AuthResult
import lnx.jetitable.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userInfoStore: UserInfoStore
) {
    suspend operator fun invoke(
        login: String,
        password: String
    ): AuthResult {
        return if (login.endsWith("@snu.edu.ua")) {
            when (val result = authRepository.login(login, password)) {
                is AuthResult.Success -> {
                    userInfoStore.saveUserInfo(result.user)
                    result
                }

                is AuthResult.Failure -> result
            }
        } else AuthResult.Failure(AuthError.NOT_CORPORATE_EMAIL)
    }
}