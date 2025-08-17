package lnx.jetitable.services.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import lnx.jetitable.api.RetrofitHolder
import lnx.jetitable.api.timetable.TimeTableApiService
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.AUTHORISATION_PHP
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.BASE_URL
import lnx.jetitable.api.timetable.data.login.AccessRequest
import lnx.jetitable.api.timetable.data.query.ClassListRequest
import lnx.jetitable.api.timetable.data.query.ExamListRequest
import lnx.jetitable.datastore.CookieDataStore
import lnx.jetitable.datastore.ScheduleDataStore
import lnx.jetitable.datastore.UserDataStore
import lnx.jetitable.screens.home.elements.datepicker.DateManager
import lnx.jetitable.services.notification.NotifManager
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.net.ConnectException

class UserDataWorker(
    context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {
    private val dateManager = DateManager()
    private val userDataStore = UserDataStore(context)
    private val cookieDataStore = CookieDataStore(context)
    private val scheduleDataStore = ScheduleDataStore(context)
    private val apiService: TimeTableApiService =
        RetrofitHolder.getTimeTableApiInstance(context)
    private val notifManager = NotifManager(context)

    override suspend fun doWork(): Result {
        return try {
            val cookies = cookieDataStore.loadForRequest("$BASE_URL/$AUTHORISATION_PHP".toHttpUrl())
            val isAuthorized = cookies.any { it.name == "tt_tokin" && it.value.isNotEmpty() }

            if (isAuthorized) {
                fetchUserData()
                fetchSchedule()
                notifManager.updateNotificationSchedules()
                Result.success()
            } else {
                Result.failure()
            }

        } catch (e: ConnectException) {
            Log.e(DATA_SYNC_WORKER_NAME, "No connection", e)
            Result.failure()
        }
    }


    private suspend fun fetchUserData() {
        val response = apiService.checkAccess(
            AccessRequest(
                TimeTableApiService.Companion.CHECK_ACCESS,
                dateManager.getSemester().toString(),
                dateManager.getAcademicYears()
            )
        )
        if (!response.accessToken.isEmpty()) {
            userDataStore.saveApiUserData(response)
        }
    }

    private suspend fun fetchSchedule() {
        val userData = userDataStore.getApiUserData().first()
        val classScheduleResponse = apiService.get_listLessonTodayStudent(
            ClassListRequest(
                TimeTableApiService.Companion.DAILY_CLASS_LIST,
                userData.group,
                userData.groupId,
                dateManager.getFormattedDate(),
                dateManager.getAcademicYears(),
                dateManager.getSemester().toString()
            )
        )
        if (classScheduleResponse.isNotEmpty()) {
            scheduleDataStore.saveClassScheduleList(classScheduleResponse)
            Log.d(SCHEDULE_FETCHER, "Class schedule saved")
        }

        val examScheduleResponse = apiService.get_sessionStudent(
            ExamListRequest(
                TimeTableApiService.Companion.EXAM_LIST,
                userData.group,
                userData.groupId,
                dateManager.getAcademicYears(),
                dateManager.getSemester().toString()
            )
        )
        if (examScheduleResponse.isNotEmpty()) {
            scheduleDataStore.saveExamScheduleList(examScheduleResponse)
            Log.d(SCHEDULE_FETCHER, "Exam schedule saved")
        }
    }

    companion object {
        private const val DATA_SYNC_WORKER_NAME = "data_sync_worker"
        private const val SCHEDULE_FETCHER = "schedule_fetcher"
    }
}