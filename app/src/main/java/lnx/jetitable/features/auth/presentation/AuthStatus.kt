package lnx.jetitable.features.auth.presentation

import lnx.jetitable.features.auth.domain.model.AuthError

sealed class AuthStatus {
    object Idle : AuthStatus()
    object Loading : AuthStatus()
    data class Error(val error: AuthError) : AuthStatus()
    object Success : AuthStatus()
}