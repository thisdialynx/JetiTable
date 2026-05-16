package lnx.jetitable.features.home.data.repository

import kotlinx.coroutines.flow.first
import lnx.jetitable.api.timetable.HtmlConverterState
import lnx.jetitable.api.timetable.TimeTableApiService
import lnx.jetitable.api.timetable.data.query.AttendanceListRequest
import lnx.jetitable.api.timetable.data.query.VerifyPresenceRequest
import lnx.jetitable.datastore.UserInfoStore
import lnx.jetitable.features.home.domain.models.AttendanceFailureReason
import lnx.jetitable.features.home.domain.models.AttendanceResult
import lnx.jetitable.features.home.domain.models.AttendanceVerificationResult
import lnx.jetitable.features.home.domain.repository.AttendanceRepository
import lnx.jetitable.features.home.presentation.ClassUiData
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceRepositoryImpl @Inject constructor(
    private val apiService: TimeTableApiService,
    private val dataStore: UserInfoStore
) : AttendanceRepository {
    override suspend fun getAttendanceList(classData: ClassUiData): AttendanceResult {
        val studentsAcademyGroupOnly = "1" // TODO: Implement switching

        return try {
            val response = apiService.get_listStudent(
                AttendanceListRequest(
                    TimeTableApiService.ATTENDANCE_LIST,
                    classData.group,
                    classData.number,
                    classData.name,
                    classData.id,
                    classData.type,
                    studentsAcademyGroupOnly,
                    classData.date,
                    classData.educatorId
                )
            )
            val responseBody = response.body()
            val data = (responseBody as HtmlConverterState.Success).data

            if (response.isSuccessful) {
                AttendanceResult.Success(data)
            } else {
                AttendanceResult.Failure(AttendanceFailureReason.NETWORK_ERROR)
            }
        } catch (e: IOException) {
            AttendanceResult.Failure(AttendanceFailureReason.NETWORK_ERROR)
        } catch (e: Exception) {
            AttendanceResult.Failure(AttendanceFailureReason.UNKNOWN_ERROR)
        }
    }

    override suspend fun verifyAttendance(classData: ClassUiData): AttendanceVerificationResult {
        val userData = dataStore.getUserInfo().first()

        return try {
            val response = apiService.get_checkZoom(
                VerifyPresenceRequest(
                    TimeTableApiService.PRESENCE_VERIFICATION,
                    TimeTableApiService.STATE,
                    userData.group,
                    userData.fullName,
                    userData.userId.toString(),
                    classData.number,
                    classData.name,
                    classData.id,
                    classData.type,
                    classData.educator,
                    classData.educatorId,
                    classData.date,
                    "${classData.start}:00",
                    "${classData.end}:00",
                )
            )

            if (response.isSuccessful) {
                AttendanceVerificationResult.Success
            } else {
                AttendanceVerificationResult.Failure(AttendanceFailureReason.UNKNOWN_ERROR)
            }
        } catch (e: IOException) {
            AttendanceVerificationResult.Failure(AttendanceFailureReason.NETWORK_ERROR)
        } catch (e: Exception) {
            AttendanceVerificationResult.Failure(AttendanceFailureReason.UNKNOWN_ERROR)
        }
    }
}