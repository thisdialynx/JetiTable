package lnx.jetitable.viewmodel

import android.app.Application
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import lnx.jetitable.datastore.UserDataStore
import lnx.jetitable.datastore.user.UserDataWorker
import lnx.jetitable.misc.DateManager
import lnx.jetitable.screens.home.data.ClassUiData
import lnx.jetitable.timetable.api.ApiService.Companion.CHECK_ZOOM
import lnx.jetitable.timetable.api.ApiService.Companion.DAILY_CLASS_LIST
import lnx.jetitable.timetable.api.ApiService.Companion.EXAM_LIST
import lnx.jetitable.timetable.api.ApiService.Companion.STATE
import lnx.jetitable.timetable.api.RetrofitHolder
import lnx.jetitable.timetable.api.query.data.ClassListRequest
import lnx.jetitable.timetable.api.query.data.Exam
import lnx.jetitable.timetable.api.query.data.ExamListRequest
import lnx.jetitable.timetable.api.query.data.VerifyPresenceRequest
import lnx.jetitable.timetable.api.query.data.parseLessonHtml
import lnx.jetitable.timetable.api.query.data.parseSessionHtml
import java.util.concurrent.TimeUnit

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val dateManager = DateManager()
    val dateState = dateManager.dateStateFlow
    private val userDataStore = UserDataStore(context)
    private val service = RetrofitHolder.getInstance(context)

    private var group by mutableStateOf<String?>(null)
    private var userId by mutableStateOf<String?>(null)
    private var fullName by mutableStateOf<String?>(null)

    private val currentTime = flow {
        while (true) {
            emit(Calendar.getInstance().timeInMillis)
            delay(60000)
        }
    }
        .flowOn(Dispatchers.IO)

    val classesFlow = combine(userDataStore.getUserData(), dateManager.selectedDate, currentTime) { userInfo, date, time ->
        getClasses(group = userInfo.group to userInfo.groupId)
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    val examsFlow = userDataStore.getUserData().map { userInfo ->
        getExams(group = userInfo.group to userInfo.groupId)
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        viewModelScope.launch {
            val workRequest = PeriodicWorkRequestBuilder<UserDataWorker>(6, TimeUnit.HOURS).build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }

        viewModelScope.launch(Dispatchers.IO) {
            userDataStore.getUserData().collect {
                group = it.group
                userId = it.userId.toString()
                fullName = it.fullName
            }
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
                val response = service.get_listLessonTodayStudent(
                    ClassListRequest(
                        DAILY_CLASS_LIST,
                        group.first,
                        group.second,
                        dateManager.getFormattedDate(),
                        dateManager.getAcademicYears(),
                        dateManager.getSemester().toString()
                    )
                )
                val parsedResponse = parseLessonHtml(response).map {
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
                        isNow = isNow
                    )
                }
                classList = parsedResponse

            } catch (e: Exception) {
                Log.e("Lesson list request", "Error in lesson list retrieval", e)
                classList = emptyList()
            }
        }
        return classList
    }

    private suspend fun getExams(group: Pair<String, String>): List<Exam> {
        var examList: List<Exam>
        withContext(Dispatchers.IO) {
            try {
                val response = service.get_sessionStudent(
                    ExamListRequest(
                        EXAM_LIST,
                        group.first,
                        group.second,
                        dateManager.getAcademicYears(),
                        dateManager.getSemester().toString()
                    )
                )
                examList = parseSessionHtml(response)
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
                        group!!,
                        fullName!!,
                        userId!!,
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