package lnx.jetitable.features.notifications.presentation

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import lnx.jetitable.datastore.AppPreferences
import lnx.jetitable.features.notifications.domain.models.NotificationPreferences
import lnx.jetitable.features.notifications.domain.usecase.SaveClassPreferences
import lnx.jetitable.features.notifications.domain.usecase.SaveExamPreferences
import lnx.jetitable.features.notifications.domain.usecase.ToggleNotificationsUseCase
import javax.inject.Inject

@HiltViewModel
class NotifViewModel @Inject constructor(
    private val appPrefs: AppPreferences,
    private val toggleNotificationsUseCase: ToggleNotificationsUseCase,
    private val saveClassPreferences: SaveClassPreferences,
    private val saveExamPreferences: SaveExamPreferences,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _hasExactAlarmPermission = MutableStateFlow(false)
    private val _hasPostNotificationsPermission = MutableStateFlow(false)
    private val _isPermissionDialogVisible = MutableStateFlow(false)
    private val notificationPreferences = combine(
        appPrefs.getClassPreferences(),
        appPrefs.getExamPreferences(),
        appPrefs.getNotificationPreference()
    ) { classPrefs, examPrefs, notifPref ->
        NotificationPreferences(classPrefs, examPrefs, notifPref)
    }
    val screenState = combine(
        _hasExactAlarmPermission,
        _hasPostNotificationsPermission,
        notificationPreferences,
        _isPermissionDialogVisible
    ) { alarmPerm, notifPerm, notifPrefs, dialogVisibility ->
        NotificationsScreenState(
            hasNotificationPermission = notifPerm,
            hasExactAlarmPermission = alarmPerm,
            isAppNotificationEnabled = notifPrefs.appNotifPref,
            isPermissionDialogVisible = dialogVisibility,
            classPrefs = notifPrefs.classPrefs,
            examPrefs = notifPrefs.examPrefs
        )
    }
        .onEach {
            val hasBothPermissions = it.hasExactAlarmPermission && it.hasNotificationPermission

            if (!hasBothPermissions) {
                val classPrefs = appPrefs.getClassPreferences().first()
                val examPrefs = appPrefs.getExamPreferences().first()
                val appNotifPref = appPrefs.getNotificationPreference().first()

                if (appNotifPref) appPrefs.saveNotificationPreference(false)
                if (classPrefs.isEnabled) appPrefs.saveClassPreferences(isEnabled = false)
                if (examPrefs.isEnabled) appPrefs.saveExamPreferences(isEnabled = false)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationsScreenState()
        )

    init {
        viewModelScope.launch {
            _hasExactAlarmPermission.value = hasExactAlarmPermission()
            _hasPostNotificationsPermission.value = hasPostNotificationPermission()
        }
    }

    fun onToggleAppNotifications(isChecked: Boolean) {
        viewModelScope.launch {
            if (!_hasExactAlarmPermission.value || !_hasPostNotificationsPermission.value) {
                _isPermissionDialogVisible.value = true
            }
            toggleNotificationsUseCase(isChecked)
        }
    }

    fun updatePermissionStatuses(hasNotif: Boolean? = null, hasAlarm: Boolean? = null) {
        hasAlarm?.let {
            _hasExactAlarmPermission.value = it
        }

        hasNotif?.let {
            _hasPostNotificationsPermission.value = it
        }
    }

    fun onPermissionDialogToggle(value: Boolean) {
        _isPermissionDialogVisible.value = value
    }

    fun onExamNotificationsToggle(value: Boolean) {
        viewModelScope.launch {
            saveExamPreferences(value)
        }
    }

    fun onClassNotificationsToggle(value: Boolean) {
        viewModelScope.launch {
            saveClassPreferences(value)
        }
    }

    fun updateExamMinutes(minutes: Int? = null) {
        viewModelScope.launch {
            appPrefs.saveExamPreferences(
                minutes = minutes
            )
        }
    }

    fun updateClassMinutes(minutes: Int? = null) {
        viewModelScope.launch {
            appPrefs.saveClassPreferences(
                minutes = minutes
            )
        }
    }

    fun hasExactAlarmPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.canScheduleExactAlarms()
        } else true
    }

    fun hasPostNotificationPermission(): Boolean {
        val notificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            notificationsEnabled && granted
        } else {
            notificationsEnabled
        }
    }
}