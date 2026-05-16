package lnx.jetitable.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import lnx.jetitable.BuildConfig
import lnx.jetitable.datastore.UserInfoStore
import lnx.jetitable.features.settings.domain.model.SettingsEntries
import lnx.jetitable.features.settings.domain.model.SettingsEvent
import lnx.jetitable.features.settings.domain.model.UpdateResult
import lnx.jetitable.features.settings.domain.model.UserProfileState
import lnx.jetitable.features.settings.domain.usecase.CheckForUpdatesUseCase
import lnx.jetitable.features.settings.domain.usecase.GetSettingsListUseCase
import lnx.jetitable.features.settings.domain.usecase.GetUserProfileUseCase
import lnx.jetitable.features.settings.domain.usecase.LogOutUseCase
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userInfoStore: UserInfoStore,
    private val logOutUseCase: LogOutUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val checkForUpdatesUseCase: CheckForUpdatesUseCase,
    private val getSettingsListUseCase: GetSettingsListUseCase
) : ViewModel() {

    private val _logOutEvent = Channel<SettingsEvent>()
    val logOutEvent = _logOutEvent.receiveAsFlow()

    private val _userProfileState = MutableStateFlow<UserProfileState>(UserProfileState.Loading)
    private val _updateState = MutableStateFlow<UpdateResult>(UpdateResult.Loading)
    private val _settingsList = MutableStateFlow<List<SettingsEntries>>(emptyList())

    init {
        viewModelScope.launch {
            userInfoStore.getUserInfo().collect {
                _userProfileState.value = getUserProfileUseCase(it)
            }
        }
        viewModelScope.launch {
            val currentVersion = BuildConfig.VERSION_NAME

            _updateState.value = checkForUpdatesUseCase(currentVersion)
        }

        _settingsList.value = getSettingsListUseCase()
    }

    val screenState = combine(
        _userProfileState,
        _updateState,
        _settingsList
    ) { user, update, settings ->
        SettingsScreenState(
            userInfo = user,
            updateInfo = update,
            settings = settings
        )
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsScreenState()
        )

    fun onLogOutClick() {
        viewModelScope.launch {
            logOutUseCase()
            _logOutEvent.send(SettingsEvent.NavigateToAuth)
        }
    }
}