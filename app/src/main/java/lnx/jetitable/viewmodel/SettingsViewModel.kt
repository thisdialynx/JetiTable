package lnx.jetitable.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import lnx.jetitable.BuildConfig
import lnx.jetitable.R
import lnx.jetitable.api.github.GithubApiService
import lnx.jetitable.datastore.AppPreferences
import lnx.jetitable.datastore.CookieDataStore
import lnx.jetitable.datastore.ScheduleDataStore
import lnx.jetitable.datastore.UserInfoStore
import lnx.jetitable.misc.AndroidConnectivityObserver
import lnx.jetitable.misc.ConnectionState
import lnx.jetitable.misc.DateManager
import javax.inject.Inject

data class UserInfoData(
    val fullName: Pair<String, Int>,
    val group: Pair<String, String>,
    val formOfEducationResId: Int,
    val academicYears: String,
    val semesterResId: Int,
    val status: String
)

data class AppUpdateInfo(
    val currentVersion: String,
    val latestVersion: String = "",
    val updateAvailable: Boolean = false,
    val downloadUrl: String = "",
    val releaseNotes: String = "",
    val error: Int? = null
)

sealed class UpdateState {
    data object Loading : UpdateState()
    data class Available(val data: AppUpdateInfo) : UpdateState()
    data object Latest : UpdateState()
    data class Failure(val reasonResId: Int, val exception: Throwable? = null) : UpdateState()
}

sealed class UserInfoState {
    data object Loading : UserInfoState()
    data class Success(val data: UserInfoData) : UserInfoState()
    data class Failure(val reasonResId: Int, val exception: Throwable? = null) : UserInfoState()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userInfoStore: UserInfoStore,
    private val cookieDataStore: CookieDataStore,
    private val scheduleDataStore: ScheduleDataStore,
    private val appPrefs: AppPreferences,
    private val dateManager: DateManager,
    private val connectivityObserver: AndroidConnectivityObserver,
    private val api: GithubApiService
): ViewModel() {
    val userInfoState = userInfoStore.getApiUserData().map {
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
        val data = UserInfoData(
            fullName = it.fullName to it.fullNameId,
            group = it.group to it.groupId,
            formOfEducationResId = formOfEducation,
            academicYears = academicYears,
            semesterResId = semester,
            status = it.status
        )

        if (data.fullName.first.isEmpty()) {
            UserInfoState.Failure(R.string.failed_to_fetch_data)
        } else {
            UserInfoState.Success(data)
        }
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserInfoState.Loading
        )

    val updateInfo = flow {
        emit(UpdateState.Loading)

        if (BuildConfig.DEBUG) {
            emit(UpdateState.Failure(R.string.debug_update_unavailable))
            return@flow
        }

        try {
            val currentVersion = BuildConfig.VERSION_NAME
            val release = api.getLatestRelease()
            val latestVersion = release.tag_name.removePrefix("v")
            val updateAvailable = isNewerVersion(latestVersion, currentVersion)
            val downloadUrl = release.assets[1].browser_download_url

            if (updateAvailable) {
                emit(
                    UpdateState.Available(
                        AppUpdateInfo(
                            currentVersion,
                            latestVersion,
                            updateAvailable,
                            downloadUrl,
                            release.body
                        )
                    )
                )
            } else UpdateState.Latest
        } catch (e: Exception) {
            UpdateState.Failure(R.string.could_not_check_for_updates, e)
        }
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UpdateState.Loading
        )

    val isConnected = connectivityObserver
        .isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ConnectionState.Loading
        )

    fun signOut() {
        viewModelScope.launch {
            userInfoStore.clearAll()
            cookieDataStore.clearCookies()
            scheduleDataStore.clearAll()
            appPrefs.clearAll()
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
}