package lnx.jetitable.viewmodel

import android.app.Application
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lnx.jetitable.BuildConfig
import lnx.jetitable.R
import lnx.jetitable.api.RetrofitHolder
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.CHECK_ZOOM
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.DAILY_CLASS_LIST
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.EXAM_LIST
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.STATE
import lnx.jetitable.api.timetable.data.login.User
import lnx.jetitable.api.timetable.data.query.ClassListRequest
import lnx.jetitable.api.timetable.data.query.ClassNetworkData
import lnx.jetitable.api.timetable.data.query.ExamListRequest
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.api.timetable.data.query.VerifyPresenceRequest
import lnx.jetitable.datastore.AppPreferences
import lnx.jetitable.datastore.ScheduleDataStore
import lnx.jetitable.datastore.UserDataStore
import lnx.jetitable.misc.AndroidConnectivityObserver
import lnx.jetitable.misc.DataState.Empty
import lnx.jetitable.misc.DataState.Error
import lnx.jetitable.misc.DataState.Loading
import lnx.jetitable.misc.DataState.Success
import lnx.jetitable.screens.home.data.ClassUiData
import lnx.jetitable.screens.home.elements.datepicker.DateManager
import lnx.jetitable.services.data.DataSyncService.Companion.DATA_SYNC_SERVICE_NAME
import lnx.jetitable.services.data.UserDataWorker

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext

    private val connectivityObserver = AndroidConnectivityObserver(context)
    private val dateManager = DateManager()
    val dateState = dateManager.dateStateFlow
    private val userDataStore = UserDataStore(context)
    private val scheduleDataStore = ScheduleDataStore(context)
    private val service = RetrofitHolder.getTimeTableApiInstance(context)
    private val appPrefs = AppPreferences(context)

    init {
        val syncRequest = OneTimeWorkRequestBuilder<UserDataWorker>()
            .addTag(DATA_SYNC_SERVICE_NAME)
            .build()

        WorkManager.getInstance(context).enqueue(syncRequest)
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
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Loading
        )

    val userData = userDataStore.getApiUserData()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = User()
        )

    val notificationTipState = appPrefs.getNotificationTipPreference()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val classesFlow = combine(
        userData,
        dateManager.selectedDate,
        currentTime,
        isConnected
    ) { userData, date, time, isConnected ->
        val currentDate = Calendar.getInstance()
        val formattedTime = dateManager.timeFormat.format(currentDate.time)
        val formattedDate = dateManager.dateFormat.format(currentDate.time)

        when (isConnected) {
            Success(false) -> {
                val storedSchedule = scheduleDataStore.getClassList().first()
                    .map { it.toUiData(formattedDate, formattedTime) }

                if (storedSchedule.isEmpty()) Error(R.string.no_internet_connection)
                    else Success(storedSchedule)
            }
            Success(true) -> {
                try {
                    val classes = getClasses(userData.group to userData.groupId)
                        .map { it.toUiData(formattedDate, formattedTime)}

                    if (classes.isEmpty()) Empty else Success(classes)
                } catch (e: Exception) {
                    val storedSchedule = scheduleDataStore.getClassList().first()
                        .map { it.toUiData(formattedDate, formattedTime) }

                    if (storedSchedule.isNotEmpty()) Success(storedSchedule)
                        else Error(R.string.schedule_load_fail, e)
                }
            }
            is Error -> Error(R.string.internet_connection_check_fail)
            is Loading -> Loading
            else -> Error(R.string.something_went_wrong)
        }
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Loading
        )

    val examsFlow = combine(userData, isConnected) { userData, isConnected ->
        when (isConnected) {
            Success(true) -> {
                try {
                    val exams = getExams(userData.group to userData.groupId)
                    if (exams.isEmpty()) Empty else Success(exams)
                } catch (e: Exception) {
                    Error(R.string.schedule_load_fail, e)
                }
            }
            Success(false) -> {
                val storedSchedule = scheduleDataStore.getExamList().first()

                if (storedSchedule.isEmpty()) Error(R.string.no_internet_connection)
                    else Success(storedSchedule)
            }
            is Error -> Error(R.string.internet_connection_check_fail)
            is Loading -> Loading
            else -> Error(R.string.something_went_wrong)
        }
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Loading
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

    private suspend fun getClasses(group: Pair<String, String>): List<ClassNetworkData> {
        var classList: List<ClassNetworkData>

        withContext(Dispatchers.IO) {
            try {
                classList = service.get_listLessonTodayStudent(
                    ClassListRequest(
                        DAILY_CLASS_LIST,
                        group.first,
                        group.second,
                        dateManager.getFormattedDate(),
                        dateManager.getAcademicYears(),
                        dateManager.getSemester().toString()
                    )
                )

            } catch (e: Exception) {
                Log.e("Lesson list request", "Error in lesson list retrieval", e)
                classList = emptyList()
            }
        }
        return classList
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

    private suspend fun getExams(group: Pair<String, String>): List<ExamNetworkData> {
        var examList: List<ExamNetworkData>
        withContext(Dispatchers.IO) {
            try {
                examList = service.get_sessionStudent(
                    ExamListRequest(
                        EXAM_LIST,
                        group.first,
                        group.second,
                        dateManager.getAcademicYears(),
                        dateManager.getSemester().toString()
                    )
                )
            } catch (e: Exception) {
                Log.e("Exam list request", "Error in exam list retrieval", e)
                examList = emptyList()
            }
        }
        return examList
    }

    fun verifyPresence(uiClass: ClassUiData) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = service.get_checkZoom(
                    VerifyPresenceRequest(
                        CHECK_ZOOM,
                        STATE,
                        userData.value.group,
                        userData.value.fullName,
                        userData.value.userId.toString(),
                        uiClass.number,
                        uiClass.name,
                        uiClass.id,
                        uiClass.type,
                        uiClass.educator,
                        uiClass.educatorId,
                        uiClass.date,
                        "${uiClass.start}:00",
                        "${uiClass.end}:00",
                    )
                )

                if (response == "ok" && BuildConfig.DEBUG) {
                    Log.d("Presence verifier", "Verification successful")
                }
            } catch (e: Exception) {
                Log.e("Presence verifier", "Verification failed", e)
            }
        }
    }
}