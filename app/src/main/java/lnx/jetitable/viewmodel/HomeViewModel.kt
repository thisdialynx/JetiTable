package lnx.jetitable.viewmodel

import android.app.Application
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
import lnx.jetitable.api.timetable.data.query.ExamListRequest
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.api.timetable.data.query.VerifyPresenceRequest
import lnx.jetitable.datastore.UserDataStore
import lnx.jetitable.datastore.user.UserDataWorker
import lnx.jetitable.misc.ConnectionState
import lnx.jetitable.misc.DateManager
import lnx.jetitable.misc.NetworkConnectivityObserver
import lnx.jetitable.misc.DataState
import lnx.jetitable.screens.home.data.ClassUiData
import java.util.concurrent.TimeUnit

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val connectivityObserver = NetworkConnectivityObserver(context)
    private val dateManager = DateManager()
    val dateState = dateManager.dateStateFlow
    private val userDataStore = UserDataStore(context)
    private val service = RetrofitHolder.getTimeTableApiInstance(context)

    private val currentTime = flow {
        while (true) {
            emit(Calendar.getInstance().timeInMillis)
            delay(60000)
        }
    }
        .flowOn(Dispatchers.IO)

    val connectivityState = connectivityObserver.observe()
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ConnectionState.Idle
        )

    val userData = userDataStore.getUserData()
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = User()
        )

    val classesFlow = combine(userData, dateManager.selectedDate, currentTime, connectivityState) { userData, date, time, connectivity ->
        when (connectivity) {
            ConnectionState.Unavailable -> DataState.Error(R.string.no_internet_connection)
            ConnectionState.Available -> {
                try {
                    val classes = getClasses(userData.group to userData.groupId)
                    if (classes.isEmpty()) DataState.Empty else DataState.Success(classes)

                } catch (e: Exception) {
                    DataState.Error(R.string.schedule_load_fail, e)
                }
            }
            ConnectionState.Idle -> DataState.Loading
        }
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DataState.Loading
        )

    val examsFlow = combine(userData, connectivityState) { userData, connectivity ->
        when (connectivity) {
            ConnectionState.Unavailable -> DataState.Error(R.string.no_internet_connection)
            ConnectionState.Available -> {
                try {
                    val exams = getExams(userData.group to userData.groupId)
                    if (exams.isEmpty()) DataState.Empty else DataState.Success(exams)

                } catch (e: Exception) {
                    DataState.Error(R.string.schedule_load_fail, e)
                }
            }
            ConnectionState.Idle -> DataState.Loading
        }
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DataState.Loading
        )

    init {
        viewModelScope.launch {
            val workRequest = PeriodicWorkRequestBuilder<UserDataWorker>(6, TimeUnit.HOURS).build()
            WorkManager.getInstance(context).enqueue(workRequest)
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

    private suspend fun getClasses(group: Pair<String, String>): List<ClassUiData> {
        var classList: List<ClassUiData>

        withContext(Dispatchers.IO) {
            val currentDate = Calendar.getInstance().apply { timeInMillis = currentTime.first() }
            val formattedTime = dateManager.timeFormat.format(currentDate.time)
            val formattedDate = dateManager.dateFormat.format(currentDate)

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
                ).map {
                    val isCurrentDate = formattedDate == it.date
                    val isTimeInRange = formattedTime in it.start..it.end
                    val isNow = isCurrentDate && isTimeInRange

                    ClassUiData(
                        id = it.id,
                        group = it.group,
                        number = it.number,
                        educator = it.educator,
                        name = it.name,
                        educatorId = it.educatorId,
                        date = it.date,
                        start = it.start,
                        end = it.end,
                        items = it.items,
                        meetingLink = it.meetingLink,
                        moodleLink = it.moodleLink,
                        type = it.type,
                        room = it.room,
                        isNow = isNow
                    )
                }

            } catch (e: Exception) {
                Log.e("Lesson list request", "Error in lesson list retrieval", e)
                classList = emptyList()
            }
        }
        return classList
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
                Log.e("Session list request", "Error in session list retrieval", e)
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