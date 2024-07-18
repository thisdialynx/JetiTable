package lnx.jetitable.timetable.api.login

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lnx.jetitable.R
import lnx.jetitable.prefdatastore.DataStoreManager
import lnx.jetitable.timetable.api.ApiService
import lnx.jetitable.timetable.api.login.data.LoginRequest
import okhttp3.Credentials
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://timetable.lond.lg.ua")
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

    var password by mutableStateOf("")
        private set
    var login by mutableStateOf("")
        private set
    var errorMessage: Int? = null


    fun checkCredentials() {
        if (!login.endsWith("@snu.edu.ua") || login.isBlank() || password.isBlank()) {
            errorMessage = R.string.corporate_email_description
            return
        }
        viewModelScope.launch {
            try {
                val basicAuth = Credentials.basic(login, password)
                val response = service.checkPassword(basicAuth,
                    LoginRequest("checkPassword", login, password)
                )
                if (response.status == "ok") {
                    dataStore.saveToken(response.token)
                }
            } catch (e: Exception) {
                errorMessage = R.string.wrong_credentials
            }
        }
    }
}