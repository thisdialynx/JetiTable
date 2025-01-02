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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lnx.jetitable.BuildConfig
import lnx.jetitable.datastore.UserDataStore
import lnx.jetitable.datastore.user.UserDataWorker
import lnx.jetitable.misc.getAcademicYear
import lnx.jetitable.misc.getFormattedDate
import lnx.jetitable.misc.getSemester
import lnx.jetitable.timetable.api.ApiService.Companion.CHECK_ZOOM
import lnx.jetitable.timetable.api.ApiService.Companion.DAILY_LESSON_LIST
import lnx.jetitable.timetable.api.ApiService.Companion.SESSION_LIST
import lnx.jetitable.timetable.api.ApiService.Companion.STATE
import lnx.jetitable.timetable.api.RetrofitHolder
import lnx.jetitable.timetable.api.query.data.Lesson
import lnx.jetitable.timetable.api.query.data.LessonListRequest
import lnx.jetitable.timetable.api.query.data.Session
import lnx.jetitable.timetable.api.query.data.SessionListRequest
import lnx.jetitable.timetable.api.query.data.VerifyPresenceRequest
import lnx.jetitable.timetable.api.query.data.parseLessonHtml
import lnx.jetitable.timetable.api.query.data.parseSessionHtml
import java.util.concurrent.TimeUnit

data class UserUiState(
    val lessonList: List<Lesson>?,
    val sessionList: List<Session>?,
    val selectedDate: Calendar
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val userDataStore = UserDataStore(context)
    private val service = RetrofitHolder.getInstance(context)

    private var group by mutableStateOf<String?>(null)
    private var userId by mutableStateOf<String?>(null)
    private var fullName by mutableStateOf<String?>(null)

    private val _selectedDate = MutableStateFlow(Calendar.getInstance())
    val selectedDate = _selectedDate.asStateFlow()

    val currentTimeFlow = flow {
        while (true) {
            emit(Calendar.getInstance().timeInMillis)
            delay(1000)
        }
    }

    val userInfoFlow = combine(userDataStore.getUserData(), selectedDate) { userInfo, date ->
        group = userInfo.group
        userId = userInfo.userId.toString()
        fullName = userInfo.fullName

        UserUiState(
            lessonList = getLessons(
                selectedDate = date,
                group = userInfo.group to userInfo.groupId
            ),
            sessionList = getSession(group = userInfo.group to userInfo.groupId),
            selectedDate = date
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserUiState(lessonList = null, sessionList = null, selectedDate = Calendar.getInstance())
    )

    init {
        viewModelScope.launch {
            val workRequest = PeriodicWorkRequestBuilder<UserDataWorker>(6, TimeUnit.HOURS).build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    fun onDateSelected(selectedDate: Calendar) {
        _selectedDate.update { selectedDate }
    }

    fun shiftDay(shift: Int) {
        val shiftedDate = (_selectedDate.value.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, shift)
        }
        if (shiftedDate.get(Calendar.YEAR) == _selectedDate.value.get(Calendar.YEAR)) {
            _selectedDate.update { shiftedDate }
        }
    }

    private suspend fun getLessons(selectedDate: Calendar, group: Pair<String, String>): List<Lesson>? {
        try {
            val response = service.get_listLessonTodayStudent(
                LessonListRequest(
                    DAILY_LESSON_LIST,
                    group.first,
                    group.second,
                    getFormattedDate(selectedDate),
                    getAcademicYear(selectedDate),
                    getSemester(selectedDate).toString()
                )
            )
            return parseLessonHtml(response)
        } catch (e: Exception) {
            Log.e("Lesson list request", "Error in lesson list retrieval", e)
            return null
        }
    }

    private suspend fun getSession(group: Pair<String, String>): List<Session>? {
        try {
            val response = service.get_sessionStudent(
                SessionListRequest(
                    SESSION_LIST,
                    group.first,
                    group.second,
                    getAcademicYear(),
                    getSemester().toString()
                )
            )
            return parseSessionHtml(response)
        } catch (e: Exception) {
            Log.e("Session list request", "Error in session list retrieval", e)
            return null
        }
    }

    fun verifyPresence(lesson: Lesson) {
        viewModelScope.launch {
            try {
                val response = service.get_checkZoom(
                    VerifyPresenceRequest(
                        CHECK_ZOOM,
                        STATE,
                        group!!,
                        fullName!!,
                        userId!!,
                        lesson.number,
                        lesson.name,
                        lesson.id,
                        lesson.type,
                        lesson.teacherFullName,
                        lesson.teacherId,
                        lesson.date,
                        "${lesson.start}:00",
                        "${lesson.end}:00",
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