package lnx.jetitable.screens.auth

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
import lnx.jetitable.BuildConfig
import lnx.jetitable.R
import lnx.jetitable.prefdatastore.DataStoreManager
import lnx.jetitable.timetable.api.ApiService
import lnx.jetitable.timetable.api.login.data.LoginRequest
import lnx.jetitable.timetable.api.login.data.MailRequest
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val client = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
        ).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(ApiService::class.java)
    private val context
        get() = getApplication<Application>().applicationContext
    private val dataStore = DataStoreManager(context)

    fun updatePassword(value: String) {
        password = value
    }
    fun updateLogin(value: String) {
        login = value
    }
    fun clearErrorMessage() {
        errorMessage = 0
    }
    private fun checkLogin(login: String): Boolean {
        return if (!login.endsWith("@snu.edu.ua")) {
            errorMessage = R.string.corporate_email_error
            true
        } else { false }
    }

    var isAuthorized by mutableStateOf(false)
        private set
    var password by mutableStateOf("")
        private set
    var login by mutableStateOf("")
        private set
    var errorMessage by mutableIntStateOf(0)
        private set


    fun checkCredentials() {
        viewModelScope.launch {
            val basicAuth = Credentials.basic(login, password)
            if (checkLogin(login)) {
                errorMessage = R.string.corporate_email_error
            } else {
                val response = service.checkPassword(basicAuth,
                    LoginRequest("checkPassword", login, password)
                )
                if (response.status == "ok") {
                    dataStore.saveToken(response.token)
                    isAuthorized = true
                } else {
                    errorMessage = R.string.wrong_credentials
                    Log.d("AuthViewModel", "isAuthorized: $isAuthorized")
                }
            }
        }
    }


    fun sendMail() {
        checkLogin(login)
        viewModelScope.launch {
            val response = service.sendMail(MailRequest("sendMail", login))
            if (response.status == "ok") {
                Toast.makeText(context, R.string.password_sent, Toast.LENGTH_SHORT).show()
            } else {
                Log.e("AuthViewModel", "Response status: ${response.status}\nResponse message: ${response.message}")
                Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_SHORT).show()
            }
        }
    }
}