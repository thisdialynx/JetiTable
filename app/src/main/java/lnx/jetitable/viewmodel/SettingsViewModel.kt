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
import lnx.jetitable.R
import lnx.jetitable.datastore.CookieDataStore
import lnx.jetitable.datastore.UserDataStore
import lnx.jetitable.misc.DateManager

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
    private val dateManager = DateManager()

    val userDataUiState = userDataStore.getUserData().map {
        val formOfEducation = if (it.isFullTime == 1) R.string.full_time else R.string.part_time
        val semester = if (dateManager.getSemester() == 1) R.string.autumn_semester else R.string.spring_semester
        val academicYears = dateManager.getAcademicYears()

        UserDataUiState(
            fullName = it.fullName to it.fullNameId,
            group = it.group to it.groupId,
            formOfEducationResId = formOfEducation,
            academicYears = academicYears,
            semesterResId = semester,
            status = it.status
        )
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun signOut() {
        viewModelScope.launch {
            userDataStore.clearDataStore()
            cookieDataStore.clearCookies()
        }
    }
}