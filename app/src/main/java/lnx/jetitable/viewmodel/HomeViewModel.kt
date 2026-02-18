@file:OptIn(ExperimentalCoroutinesApi::class)

package lnx.jetitable.viewmodel

import android.content.Context
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import lnx.jetitable.api.timetable.data.login.User
import lnx.jetitable.api.timetable.data.query.AttendanceData
import lnx.jetitable.api.timetable.data.query.ClassNetworkData
import lnx.jetitable.datastore.AppPreferences
import lnx.jetitable.datastore.UserInfoStore
import lnx.jetitable.misc.ConnectionState
import lnx.jetitable.misc.ConnectivityObserver
import lnx.jetitable.misc.DataState
import lnx.jetitable.misc.DateManager
import lnx.jetitable.repos.AttendanceRepository
import lnx.jetitable.repos.ScheduleRepository
import lnx.jetitable.repos.ScheduleState
import lnx.jetitable.screens.home.data.ClassUiData
import lnx.jetitable.services.data.DataSyncService
import lnx.jetitable.services.data.UserDataWorker
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userInfoStore: UserInfoStore,
    private val scheduleRepository: ScheduleRepository,
    private val attendanceRepository: AttendanceRepository,
    private val appPrefs: AppPreferences,
    private val dateManager: DateManager,
    private val connectivityObserver: ConnectivityObserver,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val dateState = dateManager.dateStateFlow

    init {
        val syncRequest = OneTimeWorkRequestBuilder<UserDataWorker>()
            .addTag(DataSyncService.Companion.DATA_SYNC_SERVICE_NAME)
            .build()

        WorkManager.Companion.getInstance(context).enqueue(syncRequest)
    }
    private val currentTime = flow {
        while (true) {
            emit(Calendar.getInstance().timeInMillis)
            delay(60000)
        }
    }
        .flowOn(Dispatchers.IO)

    val isConnected = connectivityObserver
        .isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = ConnectionState.Loading
        )

    val userData = userInfoStore.getApiUserData()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = User()
        )

    val notificationTipState = appPrefs.getNotificationTipPreference()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = false
        )

    private val _attendanceListState =
        MutableStateFlow<DataState<List<AttendanceData>>>(DataState.Loading)
    val attendanceListState = _attendanceListState.asStateFlow()

    val classesFlow = combine(
        userInfoStore.getApiUserData(),
        dateManager.selectedDate,
        isConnected
    ) { user, date, _ -> user to date }
        .flatMapLatest { (user, date) ->
            scheduleRepository.getClasses(user, date)
        }
        .map { dataState ->
            val currentDate = Calendar.getInstance()
            val formattedTime = dateManager.timeFormat.format(currentDate.time)
            val formattedDate = dateManager.dateFormat.format(currentDate.time)

            when (dataState) {
                is ScheduleState.Success -> {
                    val uiData = dataState.data.map { it.toUiData(formattedDate, formattedTime) }
                    ScheduleState.Success(uiData)
                }
                is ScheduleState.Loading -> ScheduleState.Loading
                is ScheduleState.Failure -> ScheduleState.Failure(dataState.reason, dataState.exception)
            }
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = ScheduleState.Loading
        )
    val examsFlow = combine(
        userInfoStore.getApiUserData(),
        isConnected
    ) { user, _ -> user }
        .flatMapLatest { user ->
            scheduleRepository.getExams(user)
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = ScheduleState.Loading
        )

    fun disableNotificationTip() {
        viewModelScope.launch(Dispatchers.IO) {
            appPrefs.saveNotificationTipPreference(false)
        }
    }

    fun shiftDayForward() {
        dateManager.updateDate(dayShift = 1)
    }

    fun shiftDayBackward() {
        dateManager.updateDate(dayShift = -1)
    }

    fun updateDate(calendar: Calendar) {
        dateManager.updateDate(calendar)
    }

    private fun ClassNetworkData.toUiData(currentDate: String, currentTime: String): ClassUiData {
        val isCurrentDate = currentDate == this.date
        val isTimeInRange = currentTime in this.start..this.end
        val isNow = isCurrentDate && isTimeInRange

        return ClassUiData(
            id = id,
            group = group,
            number = number,
            educator = educator,
            name = name,
            educatorId = educatorId,
            date = date,
            start = start,
            end = end,
            items = items,
            weeks = weeks,
            meetingLink = meetingLink,
            moodleLink = moodleLink,
            type = type,
            room = room,
            isNow = isNow
        )
    }

    fun loadAttendanceLog(classData: ClassUiData) {
        viewModelScope.launch(Dispatchers.IO) {
            attendanceRepository.getAttendanceList(classData)
                .collect { dataState ->
                    _attendanceListState.value = dataState
                }
        }
    }

    fun verifyAttendance(classData: ClassUiData) {
        viewModelScope.launch(Dispatchers.IO) {
            attendanceRepository.verifyAttendance(classData).collect()
        }
    }
}