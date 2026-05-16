package lnx.jetitable.features.auth.presentation

import lnx.jetitable.features.auth.domain.model.RecoveryError

sealed class EmailRequestStatus {
    object Idle : EmailRequestStatus()
    object Loading : EmailRequestStatus()
    data class Error(val error: RecoveryError) : EmailRequestStatus()
    object Success : EmailRequestStatus()
}