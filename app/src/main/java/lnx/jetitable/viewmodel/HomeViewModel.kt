package lnx.jetitable.viewmodel

import android.app.Application
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lnx.jetitable.BuildConfig
import lnx.jetitable.datastore.UserDataStore
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

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val userDataStore = UserDataStore(context)
    private val service = RetrofitHolder.getInstance(context)
    private val calendar = Calendar.getInstance()

    var group by mutableStateOf("")
        private set
    var groupId by mutableStateOf("")
        private set
    var selectedDate: Calendar by mutableStateOf(calendar)
        private set
    var lessonList by mutableStateOf<DailyLessonListResponse?>(null)
        private set
    

    init {
        viewModelScope.launch {
            val user: User = userDataStore.getApiUserData()
            group = user.group
            groupId = user.id_group
            getLessons(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        }
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

    private fun getLessons(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            try {
                lessonList = null

                val currentYear = getAcademicYear(year, month)
                val currentSemester = getSemester(month).toString()
                val selectedDateString = getFormattedDate(day, month + 1, year)

                val response = service.get_listLessonTodayStudent(
                    DailyLessonListRequest(
                        DAILY_LESSON_LIST,
                        group,
                        groupId,
                        selectedDateString,
                        currentYear,
                        currentSemester
                    )
                )

                lessonList = parseLessonHtml(response)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error occurred", e)
            }
        }
    }

    fun verifyPresence(lesson: Lesson) {
        viewModelScope.launch {
            try {
                val user: User = userDataStore.getApiUserData()
                val response = service.get_checkZoom(
                    VerifyPresenceRequest(
                        CHECK_ZOOM,
                        STATE,
                        user.group,
                        user.fio,
                        user.id_user.toString(),
                        lesson.numLesson,
                        lesson.lesson,
                        lesson.idLesson,
                        lesson.type,
                        lesson.fio,
                        lesson.idFio,
                        lesson.dateLes,
                        "${lesson.timeBeg}:00",
                        "${lesson.timeEnd}:00",
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