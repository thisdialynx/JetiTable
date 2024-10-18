package lnx.jetitable.viewmodel

import android.app.Application
import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lnx.jetitable.datastore.CookieDataStore
import lnx.jetitable.datastore.UserDataStore
import lnx.jetitable.misc.getAcademicYear

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val userDataStore = UserDataStore(context)
    private val cookieDataStore = CookieDataStore(context)
    private val calendar = Calendar.getInstance()

    var userId by mutableStateOf<Int?>(null)
        private set
    var fullName by mutableStateOf<String?>(null)
        private set
    var fullNameId by mutableStateOf<Int?>(null)
        private set
    var group by mutableStateOf<String?>(null)
        private set
    var groupId by mutableStateOf<String?>(null)
        private set
    var isFullTime by mutableStateOf<Boolean?>(null)
        private set
    var academicYear by mutableStateOf<String?>(null)
        private set


    init {
        viewModelScope.launch {
            val user = userDataStore.getApiUserData()
            userId = user.id_user
            fullName = user.fio
            fullNameId = user.id_fio
            group = user.group
            groupId = user.id_group
            isFullTime = user.denne == 1
            academicYear = getAcademicYear(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH + 1))
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userDataStore.clearDataStore()
            cookieDataStore.clearCookies()
        }
    }
}