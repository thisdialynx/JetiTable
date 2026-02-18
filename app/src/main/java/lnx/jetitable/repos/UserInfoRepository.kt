package lnx.jetitable.repos

import lnx.jetitable.api.timetable.HtmlConverterState
import lnx.jetitable.api.timetable.TimeTableApiService
import lnx.jetitable.api.timetable.data.login.AccessRequest
import lnx.jetitable.api.timetable.data.login.AccessResponse
import lnx.jetitable.datastore.UserInfoStore
import lnx.jetitable.misc.DateManager
import javax.inject.Inject
import javax.inject.Singleton

interface UserInfoRepository {
    suspend fun refreshUserInfo(): UserInfoState
}

sealed class UserInfoState {
    data class Success(val data: AccessResponse) : UserInfoState()
    data class Failure(val e: Throwable?) : UserInfoState()
}

@Singleton
class UserInfoRepositoryImpl @Inject constructor(
    private val apiService: TimeTableApiService,
    private val userInfoStore: UserInfoStore,
    private val dateManager: DateManager
) : UserInfoRepository {
    override suspend fun refreshUserInfo(): UserInfoState {
        return try {
            val response = apiService.checkAccess(
                AccessRequest(
                    TimeTableApiService.CHECK_ACCESS,
                    dateManager.getSemester().toString(),
                    dateManager.getAcademicYears()
                )
            )

            if (response == HtmlConverterState.Empty) {
                UserInfoState.Failure(Exception("Access token is empty"))
            } else {
                val data = (response as HtmlConverterState.Success).data
                userInfoStore.saveApiUserData(data)
                UserInfoState.Success(data)
            }
        } catch (e: Exception) {
            UserInfoState.Failure(e)
        }
    }
}