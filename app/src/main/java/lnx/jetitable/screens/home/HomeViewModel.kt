package lnx.jetitable.screens.home

import android.app.Application
import android.content.res.Resources.NotFoundException
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lnx.jetitable.datastore.UserDataManager
import lnx.jetitable.timetable.api.dailyLessonListDataExtractor
import lnx.jetitable.misc.getAcademicYear
import lnx.jetitable.misc.getCurrentDate
import lnx.jetitable.misc.getSemester
import lnx.jetitable.timetable.api.ApiService.Companion.DAILY_LESSON_LIST
import lnx.jetitable.timetable.api.RetrofitHolder
import lnx.jetitable.timetable.api.login.data.User
import lnx.jetitable.timetable.api.query.data.DailyLessonListRequest
import lnx.jetitable.timetable.api.query.data.DailyLessonListResponse

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val userDataStore = UserDataManager(context)
    private val service = RetrofitHolder.getInstance(context)

    var fullName by mutableStateOf<String?>(null)
        private set
    var group by mutableStateOf("")
        private set
    var groupId by mutableStateOf("")
        private set
    var dailyLessonList by mutableStateOf<DailyLessonListResponse?>(null)
        private set
    

    init {
        viewModelScope.launch {
            val user: User = userDataStore.getApiUserData()
            fullName = user.fio
            group = user.group
            groupId = user.id_group
        }
    }

    fun getDailyLessonList() {
        viewModelScope.launch {
            try {
                val calendar = Calendar.getInstance()
                val currentDate = getCurrentDate()

                val currentYear = getAcademicYear(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1
                )
                val currentSemester = getSemester(calendar.get(Calendar.MONTH) + 1)
                val firstGroupId = groupId.split(",")[0].trim()

                val response = service.get_listLessonTodayStudent(
                    DailyLessonListRequest(
                        DAILY_LESSON_LIST,
                        group,
                        firstGroupId,
                        currentDate,
                        currentYear,
                        currentSemester
                    )
                )

                dailyLessonList = dailyLessonListDataExtractor(response)
            } catch (e: NotFoundException) {
                Log.d("HomeViewModel", "Page not found", e)
            } catch (e: Exception) {
                Log.d("HomeViewModel", "Error occurred", e)
            }
        }
    }
}