package lnx.jetitable.services.data

import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.AUTHORISATION_PHP
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.BASE_URL
import lnx.jetitable.datastore.CookieDataStore
import lnx.jetitable.datastore.UserInfoStore
import lnx.jetitable.repos.ScheduleFailureReason
import lnx.jetitable.repos.ScheduleRepository
import lnx.jetitable.repos.ScheduleState
import lnx.jetitable.repos.UserInfoRepository
import lnx.jetitable.repos.UserInfoState
import lnx.jetitable.services.notification.NotifManager
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.net.ConnectException

@HiltWorker
class UserDataWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val userInfoStore: UserInfoStore,
    private val cookieDataStore: CookieDataStore,
    private val scheduleRepository: ScheduleRepository,
    private val userInfoRepository: UserInfoRepository,
    private val notifManager: NotifManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val cookies = cookieDataStore.loadForRequest("$BASE_URL/$AUTHORISATION_PHP".toHttpUrl())
            val isAuthorized = cookies.any { it.name == "tt_tokin" && it.value.isNotEmpty() }

            if (isAuthorized) {
                val userInfoSuccess = fetchUserData()
                val scheduleSuccess = fetchSchedule()

                if (scheduleSuccess && userInfoSuccess) {
                    notifManager.updateNotificationSchedules()
                    Result.success()
                } else Result.retry()
            } else {
                Result.failure()
            }

        } catch (e: ConnectException) {
            Log.e(DATA_SYNC_WORKER_NAME, "No connection", e)
            Result.failure()
        } catch (e: Exception) {
            Log.e(DATA_SYNC_WORKER_NAME, "Unable to sync data", e)
            Result.failure()
        }
    }

    private suspend fun fetchUserData(): Boolean {
        return when (val result = userInfoRepository.refreshUserInfo()) {
            is UserInfoState.Success -> {
                Log.d(USER_INFO_FETCHER, "User info refreshed successfully")
                true
            }
            is UserInfoState.Failure -> {
                Log.e(USER_INFO_FETCHER, "Failed to refresh user info", result.e)
                false
            }
        }
    }

    private suspend fun fetchSchedule(): Boolean {
        val userData = userInfoStore.getApiUserData().first()

        if (userData.group.isEmpty()) {
            Log.w(SCHEDULE_FETCHER, "Skipping schedule fetch because user data is missing")
            return false
        }
        val today = Calendar.getInstance()

        val classResponse = scheduleRepository.refreshClasses(userData, today)
        val examResponse = scheduleRepository.refreshExams(userData)

        val wasClassesSuccess = classResponse !is ScheduleState.Failure || classResponse.reason == ScheduleFailureReason.EMPTY
        val wasExamsSuccess = examResponse !is ScheduleState.Failure || examResponse.reason == ScheduleFailureReason.EMPTY

        if (!wasClassesSuccess) Log.e(SCHEDULE_FETCHER, "Failed to refresh classes: ${classResponse.reason}")
        if (!wasExamsSuccess) Log.e(SCHEDULE_FETCHER, "Failed to refresh exams: ${examResponse.reason}")

        return wasClassesSuccess || wasExamsSuccess
    }

    companion object {
        private const val DATA_SYNC_WORKER_NAME = "data_sync_worker"
        private const val SCHEDULE_FETCHER = "schedule_fetcher"
        private const val USER_INFO_FETCHER = "user_info_fetcher"
    }
}