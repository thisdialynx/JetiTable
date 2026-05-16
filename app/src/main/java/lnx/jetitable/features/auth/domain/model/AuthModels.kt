package lnx.jetitable.features.auth.domain.model

import lnx.jetitable.api.timetable.domain.models.User

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Failure(val error: AuthError) : AuthResult()
}

sealed class RecoveryResult {
    object Success : RecoveryResult()
    data class Failure(val error: RecoveryError) : RecoveryResult()
}

enum class AuthError {
    INVALID_CREDENTIALS,
    NOT_CORPORATE_EMAIL,
    NETWORK_ERROR,
    UNKNOWN_ERROR,
    EMPTY_RESPONSE
}

enum class RecoveryError {
    INVALID_CREDENTIALS,
    NOT_CORPORATE_EMAIL,
    NETWORK_ERROR,
    UNKNOWN_ERROR
}