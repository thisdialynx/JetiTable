package lnx.jetitable.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import lnx.jetitable.BuildConfig
import lnx.jetitable.R
import lnx.jetitable.api.RetrofitHolder
import lnx.jetitable.datastore.AppPreferences
import lnx.jetitable.datastore.CookieDataStore
import lnx.jetitable.datastore.ScheduleDataStore
import lnx.jetitable.datastore.UserDataStore
import lnx.jetitable.misc.AndroidConnectivityObserver
import lnx.jetitable.misc.DataState
import lnx.jetitable.misc.DataState.Error
import lnx.jetitable.screens.home.elements.datepicker.DateManager

data class UserDataUiState(
    val fullName: Pair<String, Int>,
    val group: Pair<String, String>,
    val formOfEducationResId: Int,
    val academicYears: String,
    val semesterResId: Int,
    val status: String
)

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val userDataStore = UserDataStore(context)
    private val cookieDataStore = CookieDataStore(context)
    private val scheduleDataStore = ScheduleDataStore(context)
    private val dateManager = DateManager()
    private val connectivityObserver = AndroidConnectivityObserver(context)
    private val githubService = RetrofitHolder.getGitHubApiInstance()
    private val appPreferences = AppPreferences(context)

    val userDataUiState = userDataStore.getApiUserData().map {
        val formOfEducation = when (it.isFullTime) {
            0 -> R.string.part_time
            1 -> R.string.full_time
            else -> 0
        }
        val semester = when (dateManager.getSemester()) {
            1 -> R.string.autumn_semester
            2 -> R.string.spring_semester
            else -> 0
        }
        val academicYears = dateManager.getAcademicYears()

        val userUiData = UserDataUiState(
            fullName = it.fullName to it.fullNameId,
            group = it.group to it.groupId,
            formOfEducationResId = formOfEducation,
            academicYears = academicYears,
            semesterResId = semester,
            status = it.status
        )

        if (userUiData.fullName.first == "") {
            Error(R.string.account_data_fetch_failure)
        } else {
            DataState.Success(userUiData)
        }
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = DataState.Loading
        )

    val updateInfo = connectivityObserver
        .isConnected
        .map {
            when (it) {
                DataState.Loading -> DataState.Loading
                DataState.Success(false) -> Error(R.string.no_internet_connection)
                DataState.Success(true) -> {
                    try {
                        val currentVersion = BuildConfig.VERSION_NAME
                        val isDebug = BuildConfig.DEBUG
                        val release = githubService.getLatestRelease()
                        val latestVersion = release.tag_name.removePrefix("v")
                        val updateAvailable = isNewerVersion(latestVersion, currentVersion)
                        val downloadUrl = if (isDebug) release.assets[0].browser_download_url else release.assets[1].browser_download_url

                        if (updateAvailable) {
                            DataState.Success(
                                AppUpdateInfo(currentVersion, latestVersion, updateAvailable, downloadUrl, release.body)
                            )
                        } else {
                            DataState.Empty
                        }
                    } catch (e: Exception) {
                        Error(R.string.could_not_check_for_updates, e)
                    }
                }
                is Error -> Error(R.string.internet_connection_check_fail)
                else -> Error(R.string.something_went_wrong)
            }
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DataState.Loading
        )

    fun signOut() {
        viewModelScope.launch {
            userDataStore.clearAll()
            cookieDataStore.clearCookies()
            scheduleDataStore.clearAll()
            appPreferences.clearAll()
        }
    }

    private fun isNewerVersion(latestVersion: String, currentVersion: String): Boolean {
        val latest = latestVersion.split(".").map { it.toIntOrNull() ?: 0 }
        val current = currentVersion.split(".").map { it.toIntOrNull() ?: 0 }

        for (i in 0 until minOf(latest.size, current.size)) {
            if (latest[i] > current[i]) return true
            if (latest[i] < current[i]) return false
        }
        return latest.size > current.size
    }

    data class AppUpdateInfo(
        val currentVersion: String,
        val latestVersion: String = "",
        val updateAvailable: Boolean = false,
        val downloadUrl: String = "",
        val releaseNotes: String = "",
        val error: Int? = null
    )
}