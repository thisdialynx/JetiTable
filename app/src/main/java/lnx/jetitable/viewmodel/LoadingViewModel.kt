package lnx.jetitable.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lnx.jetitable.datastore.CookieDataStore
import lnx.jetitable.timetable.api.ApiService.Companion.AUTHORISATION_PHP
import lnx.jetitable.timetable.api.ApiService.Companion.BASE_URL
import okhttp3.HttpUrl.Companion.toHttpUrl

class LoadingViewModel(application: Application) : AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val dataStore = CookieDataStore(context)
    var isAuthorized by mutableStateOf<Boolean?>(null)
        private set

    fun checkToken() {
        viewModelScope.launch {
            val cookies = dataStore.loadForRequest( "$BASE_URL/$AUTHORISATION_PHP".toHttpUrl())
            isAuthorized = cookies.any { it.name == "tt_tokin" && it.value.isNotEmpty() }
            Log.d("LoadingViewModel", "isAuthorized: $isAuthorized")
        }
    }
}