package lnx.jetitable.api.timetable.data.repository

import lnx.jetitable.api.timetable.HtmlConverterState
import lnx.jetitable.api.timetable.TimeTableApiService
import lnx.jetitable.api.timetable.data.login.AccessRequest
import lnx.jetitable.api.timetable.domain.models.InfoRequestFailureReason
import lnx.jetitable.api.timetable.domain.models.SemesterType
import lnx.jetitable.api.timetable.domain.models.UserInfoState
import lnx.jetitable.api.timetable.domain.models.UserInfoState.*
import lnx.jetitable.api.timetable.domain.repository.UserInfoRepository
import lnx.jetitable.datastore.UserInfoStore
import lnx.jetitable.misc.DateHelper
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoRepositoryImpl @Inject constructor(
    private val apiService: TimeTableApiService,
    private val userInfoStore: UserInfoStore,
    private val dateHelper: DateHelper
) : UserInfoRepository {
    override suspend fun refreshUserInfo(): UserInfoState {
        return try {
            val semester = if (dateHelper.getSemester() == SemesterType.AUTUMN) 1 else 2

            val response = apiService.checkAccess(
                AccessRequest(
                    TimeTableApiService.CHECK_ACCESS,
                    semester.toString(),
                    dateHelper.getAcademicYears()
                )
            )

            if (response.isSuccessful) {
                when (val body = response.body()) {
                    is HtmlConverterState.Success -> {
                        userInfoStore.saveUserInfo(body.data.user)
                        Success(body.data)
                    }

                    HtmlConverterState.Empty -> {
                        Failure(InfoRequestFailureReason.EMPTY_TOKEN)
                    }

                    else -> {
                        Failure(InfoRequestFailureReason.PARSING_ERROR)
                    }
                }
            } else {
                Failure(InfoRequestFailureReason.NETWORK_ERROR)
            }

        } catch (e: IOException) {
            Failure(InfoRequestFailureReason.IO_FAIL)
        } catch (e: Exception) {
            Failure(InfoRequestFailureReason.UNKNOWN_ERROR)
        }
    }
}