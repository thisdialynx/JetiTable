package lnx.jetitable.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import lnx.jetitable.R
import lnx.jetitable.misc.ConnectionState
import lnx.jetitable.misc.NetworkConnectivityObserver
import lnx.jetitable.timetable.api.ApiService.Companion.CHECK_PASSWORD
import lnx.jetitable.timetable.api.ApiService.Companion.SEND_MAIL
import lnx.jetitable.timetable.api.RetrofitHolder
import lnx.jetitable.timetable.api.login.data.LoginRequest
import lnx.jetitable.timetable.api.login.data.MailRequest
import okhttp3.Credentials

sealed class AuthState {
    object Authorized : AuthState()
    data class Error(val messageResIs: Int) : AuthState()
    object Idle : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val service = RetrofitHolder.getInstance(context)
    val connectivityObserver = NetworkConnectivityObserver(context)
    val connectivityState = connectivityObserver.observe()
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ConnectionState.Unavailable
        )

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set
    var password by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set

    fun updatePassword(value: String) { password = value }
    fun updateEmail(value: String) { email = value }
    fun clearErrorMessage() { authState = AuthState.Idle }

    private fun checkEmail(login: String): Boolean {
        return if (!login.endsWith("@snu.edu.ua")) {
            authState = AuthState.Error(R.string.corporate_email_error)
            true
        } else false
    }

    fun checkCredentials() {
        viewModelScope.launch {
            if (connectivityState.value != ConnectionState.Available) {
                authState = AuthState.Error(R.string.no_internet_connection)
                return@launch
            }

            try {

                if (checkEmail(email)) return@launch

                val basicAuth = Credentials.basic(email, password)
                val response = service.checkPassword(
                    basicAuth,
                    LoginRequest(CHECK_PASSWORD, email, password)
                )
                if (response.status == "ok") {
                    authState = AuthState.Authorized
                } else {
                    authState = AuthState.Error(R.string.wrong_credentials)
                }

            } catch (e: Exception) {
                Log.e("TimeTable authentication", "Login process error.\nisAuthorized: $authState", e)
            }
        }
    }

    fun sendMail() {
        viewModelScope.launch {
            if (connectivityState.value != ConnectionState.Available) {
                authState = AuthState.Error(R.string.no_internet_connection)
                return@launch
            }

            try {
                if (checkEmail(email)) return@launch

                val response = service.sendMail(MailRequest(SEND_MAIL, email))
                if (response.status == "ok") {
                    Toast.makeText(context, R.string.password_sent, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("Password recovery", "Email sending error", e)
            }
        }
    }
}