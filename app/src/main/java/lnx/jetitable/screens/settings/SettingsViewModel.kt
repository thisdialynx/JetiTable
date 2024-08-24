package lnx.jetitable.screens.settings

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lnx.jetitable.datastore.CookieManager
import lnx.jetitable.datastore.UserDataManager

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val userDataStore = UserDataManager(context)
    private val cookieManager = CookieManager(context)

    var userId by mutableStateOf<String?>(null)
        private set
    var fullName by mutableStateOf<String?>(null)
        private set
    var fullNameId by mutableStateOf<String?>(null)
        private set
    var group by mutableStateOf<String?>(null)
        private set
    var groupId by mutableStateOf<String?>(null)
        private set
    var isFullTime by mutableStateOf<Boolean?>(null)
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
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userDataStore.clearDataStore()
            cookieManager.clearCookies()
        }
    }
}