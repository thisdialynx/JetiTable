package lnx.jetitable.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import lnx.jetitable.features.auth.domain.model.AuthResult
import lnx.jetitable.features.auth.domain.model.RecoveryResult
import lnx.jetitable.features.auth.domain.usecase.LoginUseCase
import lnx.jetitable.features.auth.domain.usecase.SendEmailUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val sendEmailUseCase: SendEmailUseCase
) : ViewModel() {
    private val _login = MutableStateFlow("")
    val login = _login.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _showWarningDialog = MutableStateFlow(false)
    val showWarningDialog = _showWarningDialog.asStateFlow()

    private val _showPasswordRecoverDialog = MutableStateFlow(false)
    val showPasswordRecoverDialog = _showPasswordRecoverDialog.asStateFlow()

    private val _authStatus = MutableStateFlow<AuthStatus>(AuthStatus.Idle)
    val authState = _authStatus.asStateFlow()

    private val _emailRequestState = MutableStateFlow<EmailRequestStatus>(EmailRequestStatus.Idle)
    val emailRequestState = _emailRequestState.asStateFlow()

    fun onLoginClick() {
        viewModelScope.launch {
            _authStatus.value = AuthStatus.Loading

            when (val result = loginUseCase(_login.value, _password.value)) {
                is AuthResult.Success -> _authStatus.value = AuthStatus.Success
                is AuthResult.Failure -> _authStatus.value = AuthStatus.Error(result.error)
            }
        }
    }

    fun onEmailSend() {
        viewModelScope.launch {
            _emailRequestState.value = EmailRequestStatus.Loading

            when (val result = sendEmailUseCase(_login.value)) {
                is RecoveryResult.Success -> _emailRequestState.value = EmailRequestStatus.Success
                is RecoveryResult.Failure -> _emailRequestState.value =
                    EmailRequestStatus.Error(result.error)
            }
        }
    }

    fun updateLogin(value: String) {
        _login.value = value
    }

    fun updatePassword(value: String) {
        _password.value = value
    }

    fun updateWarningDialogState(value: Boolean) {
        _showWarningDialog.value = value
    }

    fun updatePasswordRecoverDialogState(value: Boolean) {
        _showPasswordRecoverDialog.value = value
    }
}