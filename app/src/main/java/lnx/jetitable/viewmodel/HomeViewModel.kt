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
import kotlinx.coroutines.launch
import lnx.jetitable.BuildConfig
import lnx.jetitable.datastore.UserDataStore
import lnx.jetitable.datastore.user.UserDataWorker
import lnx.jetitable.misc.currentDay
import lnx.jetitable.misc.currentMonth
import lnx.jetitable.misc.currentYear
import lnx.jetitable.misc.getAcademicYear
import lnx.jetitable.misc.getFormattedDate
import lnx.jetitable.misc.getSemester
import lnx.jetitable.timetable.api.ApiService.Companion.CHECK_ZOOM
import lnx.jetitable.timetable.api.ApiService.Companion.DAILY_LESSON_LIST
import lnx.jetitable.timetable.api.ApiService.Companion.STATE
import lnx.jetitable.timetable.api.RetrofitHolder
import lnx.jetitable.timetable.api.login.data.User
import lnx.jetitable.timetable.api.parseLessonHtml
import lnx.jetitable.timetable.api.query.data.DailyLessonListRequest
import lnx.jetitable.timetable.api.query.data.DailyLessonListResponse
import lnx.jetitable.timetable.api.query.data.Lesson
import lnx.jetitable.timetable.api.query.data.VerifyPresenceRequest
import java.util.concurrent.TimeUnit

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val userDataStore = UserDataStore(context)
    private val service = RetrofitHolder.getInstance(context)

    var selectedDate: Calendar by mutableStateOf(Calendar.getInstance())
        private set
    var lessonList by mutableStateOf<LessonListResponse?>(null)
        private set
    var selectedDate: Calendar by mutableStateOf(Calendar.getInstance())
    var group by mutableStateOf<String?>(null)
        private set
    var groupId by mutableStateOf<String?>(null)
        private set
    var userId by mutableStateOf<String?>(null)
        private set
    var fullName by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            scheduleUserDataWorker()
            userDataFlow.map {
                group = it.group
                groupId = it.id_group
                userId = it.id_user.toString()
                fullName = it.fio
            }.collect {
                getSession()
                getLessons()
            }
        }
    }

    private fun scheduleUserDataWorker() {
        val workRequest = PeriodicWorkRequestBuilder<UserDataWorker>(6, TimeUnit.HOURS).build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    fun onDateSelected(
        year: Int = selectedDate.get(Calendar.YEAR),
        month: Int = selectedDate.get(Calendar.MONTH),
        day: Int = selectedDate.get(Calendar.DAY_OF_MONTH),
        shift: Int = 0
    ) {
        selectedDate.set(year, month, day)
        selectedDate.add(Calendar.DAY_OF_MONTH, shift)
        getLessons(
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun getLessons(
        year: Int = selectedDate.get(Calendar.YEAR),
        month: Int = selectedDate.get(Calendar.MONTH),
        day: Int = selectedDate.get(Calendar.DAY_OF_MONTH)
    ) {
        viewModelScope.launch {
            try {
                lessonList = null

                val response = service.get_listLessonTodayStudent(
                    DailyLessonListRequest(
                        DAILY_LESSON_LIST,
                        group,
                        groupId,
                        getFormattedDate(day, month + 1, year),
                        getAcademicYear(year, month),
                        getSemester(month).toString()
                    )
                )

                lessonList = parseLessonHtml(response)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error occurred", e)
    private fun getSession() {
        viewModelScope.launch {
            try {
                sessionList = null

                val response = service.get_sessionStudent(
                    SessionListRequest(
                        SESSION_LIST,
                        group!!,
                        groupId!!,
                        getAcademicYear(),
                        getSemester().toString()
                    )
                )

                sessionList = parseSessionHtml(response)
            }
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
                    Log.d("HomeViewModel", "Request successful")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Request send error", e)
            }
        }
    }
}