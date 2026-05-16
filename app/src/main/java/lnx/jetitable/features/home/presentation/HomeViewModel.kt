@file:OptIn(ExperimentalCoroutinesApi::class)

package lnx.jetitable.features.home.presentation

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.datastore.AppPreferences
import lnx.jetitable.datastore.UserInfoStore
import lnx.jetitable.features.home.domain.models.AttendanceResult
import lnx.jetitable.features.home.domain.models.ScheduleResult
import lnx.jetitable.features.home.domain.models.ScheduleState
import lnx.jetitable.features.home.domain.repository.AttendanceRepository
import lnx.jetitable.features.home.domain.repository.ScheduleRepository
import lnx.jetitable.features.home.domain.usecase.EnqueueSyncRequestUseCase
import lnx.jetitable.features.home.domain.usecase.GetClassListUseCase
import lnx.jetitable.features.home.presentation.elements.HomeScreenState
import lnx.jetitable.misc.DateHelper
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appPrefs: AppPreferences,
    private val dateHelper: DateHelper,
    private val scheduleRepository: ScheduleRepository,
    private val attendanceRepository: AttendanceRepository,
    private val userInfoStore: UserInfoStore,
    private val getClassListUseCase: GetClassListUseCase,
    private val enqueueSyncRequestUseCase: EnqueueSyncRequestUseCase
) : ViewModel() {
    private val dateState = dateHelper.selectedDateInfo
    private val _classListState =
        MutableStateFlow<ScheduleResult<List<ClassUiData>>>(ScheduleResult.Loading)
    private val _examListState =
        MutableStateFlow<ScheduleResult<List<ExamNetworkData>>>(ScheduleResult.Loading)
    private val _attendanceListState = MutableStateFlow<AttendanceResult>(AttendanceResult.Idle)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val currentTime = flow {
        while (true) {
            emit(Calendar.getInstance().timeInMillis)
            delay(60000)
        }
    }
        .flowOn(Dispatchers.IO)

    private val notificationTipState = appPrefs.getNotificationTipPreference()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val scheduleData = combine(
        _classListState,
        _examListState,
    ) { classes, exams ->
        ScheduleState(classes, exams)
    }

    private val userData = userInfoStore.getUserInfo()
    val screenState = combine(
        scheduleData,
        _attendanceListState,
        userData,
        dateState,
        notificationTipState
    ) { schedule, attendance, user, date, tip ->
        HomeScreenState(
            schedule.classList,
            schedule.examList,
            attendance,
            user.fullName,
            tip,
            date
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeScreenState()
    )

    init {
        viewModelScope.launch {
            dateHelper.selectedDate
                .collect {
                    loadClasses(it)
                }
        }

        enqueueSyncRequestUseCase()
    }

    fun disableNotificationTip() {
        viewModelScope.launch(Dispatchers.IO) {
            appPrefs.saveNotificationTipPreference(false)
        }
    }

    fun onForwardDayShift() {
        dateHelper.updateDate(dayShift = 1)
    }

    fun onBackwardDayShift() {
        dateHelper.updateDate(dayShift = -1)
    }

    fun onDateUpdate(calendar: Calendar) {
        dateHelper.updateDate(calendar)
    }

    fun loadAttendanceLog(classData: ClassUiData) {
        viewModelScope.launch(Dispatchers.IO) {
            _attendanceListState.value = attendanceRepository.getAttendanceList(classData)
        }
    }

    fun onPresenceVerify(classData: ClassUiData) {
        viewModelScope.launch(Dispatchers.IO) {
            attendanceRepository.verifyAttendance(classData)
        }
    }

    fun refreshSchedules() {
        viewModelScope.launch {
            _isRefreshing.value = true

            try {
                val currentDate = dateHelper.selectedDate.value

                loadClasses(currentDate)
                loadExams()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private suspend fun loadClasses(calendar: Calendar) {
        _classListState.value = ScheduleResult.Loading
        _classListState.value = getClassListUseCase(calendar)
    }

    private suspend fun loadExams() {
        _examListState.value = ScheduleResult.Loading
        _examListState.value = scheduleRepository.getExams()
    }
}