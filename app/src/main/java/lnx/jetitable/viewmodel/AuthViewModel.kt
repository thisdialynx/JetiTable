package lnx.jetitable.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lnx.jetitable.R
import lnx.jetitable.timetable.api.ApiService.Companion.CHECK_PASSWORD
import lnx.jetitable.timetable.api.ApiService.Companion.SEND_MAIL
import lnx.jetitable.timetable.api.RetrofitHolder
import lnx.jetitable.timetable.api.login.data.LoginRequest
import lnx.jetitable.timetable.api.login.data.MailRequest
import okhttp3.Credentials

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val service = RetrofitHolder.getInstance(context)

    var isAuthorized by mutableStateOf(false)
        private set
    var password by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var errorMessage by mutableIntStateOf(0)
        private set

    fun updatePassword(value: String) { password = value }
    fun updateEmail(value: String) { email = value }
    fun clearErrorMessage() { errorMessage = 0 }
    private fun checkEmail(login: String): Boolean {
        return if (!login.endsWith("@snu.edu.ua")) {
            errorMessage = R.string.corporate_email_error
            true
        } else { false }
    }

    fun checkCredentials() {
        viewModelScope.launch {
            try {
                val basicAuth = Credentials.basic(email, password)

                if (checkEmail(email)) {
                    errorMessage = R.string.corporate_email_error
                } else {
                    val response = service.checkPassword(
                        basicAuth,
                        LoginRequest(CHECK_PASSWORD, email, password)
                    )

                    if (response.status == "ok") {
                        isAuthorized = true
                    } else { errorMessage = R.string.wrong_credentials }
                }
            } catch (e: Exception) {
                Log.e("TimeTable authentication", "Login process error.\nisAuthorized: $isAuthorized", e)
            }
        }

    }

    fun sendMail() {
        checkEmail(email)
        viewModelScope.launch {
            try {
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