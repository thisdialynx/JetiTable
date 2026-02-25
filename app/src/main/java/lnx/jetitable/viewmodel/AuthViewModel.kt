package lnx.jetitable.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import lnx.jetitable.R
import lnx.jetitable.api.timetable.TimeTableApiService
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.CHECK_PASSWORD
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.SEND_MAIL
import lnx.jetitable.api.timetable.data.login.LoginRequest
import lnx.jetitable.api.timetable.data.login.MailRequest
import lnx.jetitable.misc.AndroidConnectivityObserver
import lnx.jetitable.misc.DataState
import okhttp3.Credentials
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiService: TimeTableApiService,
    private val connectivityObserver: AndroidConnectivityObserver,
) : ViewModel() {
    val isConnected = connectivityObserver
        .isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DataState.Loading
        )

    var authState by mutableStateOf<DataState<Boolean>>(DataState.Empty)
        private set
    var emailRequestState by mutableStateOf<DataState<Int>>(DataState.Empty)
    var password by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set

    fun updatePassword(value: String) { password = value }
    fun updateEmail(value: String) { email = value }

    private fun checkEmail(login: String): Boolean {
        return if (!login.endsWith("@snu.edu.ua")) {
            authState = DataState.Error(R.string.corporate_email_error)
            true
        } else false
    }

    fun checkCredentials() {
        viewModelScope.launch {
            authState = DataState.Loading

            if (isConnected.value == DataState.Success(false)) {
                authState = DataState.Error(R.string.no_internet_connection)
                return@launch
            }

            try {
                if (checkEmail(email)) return@launch

                val basicAuth = Credentials.basic(email, password)
                val response = apiService.checkPassword(
                    basicAuth,
                    LoginRequest(CHECK_PASSWORD, email, password)
                )
                authState = if (response.status == "ok") {
                    DataState.Success(true)
                } else {
                    DataState.Error(R.string.wrong_credentials)
                }

            } catch (e: Exception) {
                Log.e("TimeTable authentication", "Login process error.\nisAuthorized: $authState", e)
            }
        }
    }

    fun sendEmail() {
        viewModelScope.launch {
            if (isConnected.value == DataState.Success(false)) {
                authState = DataState.Error(R.string.no_internet_connection)
                return@launch
            }

            try {
                if (checkEmail(email)) return@launch

                val response = apiService.sendMail(MailRequest(SEND_MAIL, email))
                emailRequestState = if (response.status == "ok") {
                    DataState.Success(R.string.password_sent)
                } else {
                    DataState.Error(R.string.invalid_email)
                }
            } catch (e: Exception) {
                Log.e("Password recovery", "Email sending error", e)
            }
        }
    }
}