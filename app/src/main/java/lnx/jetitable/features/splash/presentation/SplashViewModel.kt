package lnx.jetitable.features.splash.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.AUTHORISATION_PHP
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.BASE_URL
import lnx.jetitable.datastore.CookieDataStore
import lnx.jetitable.features.splash.domain.model.SplashState
import lnx.jetitable.misc.AndroidSyncManager
import okhttp3.HttpUrl.Companion.toHttpUrl
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val cookieDataStore: CookieDataStore,
    private val androidSyncManager: AndroidSyncManager
) : ViewModel() {

    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val screenState = _splashState.asStateFlow()

    init {
        checkToken()
    }

    private fun checkToken() {
        viewModelScope.launch {
            delay(1)

            val cookies = cookieDataStore.loadForRequest("$BASE_URL/$AUTHORISATION_PHP".toHttpUrl())

            _splashState.value =
                if (cookies.any { it.name == "tt_tokin" && it.value.isNotEmpty() }) {
                    androidSyncManager.startSync()
                    SplashState.Authorized
                } else {
                    androidSyncManager.startSync()
                    SplashState.Unauthorized
                }

            Timber.d("isAuthorized: ${_splashState.value}")
        }
    }
}