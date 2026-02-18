package lnx.jetitable.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import lnx.jetitable.R
import lnx.jetitable.api.timetable.HtmlConverterState
import lnx.jetitable.api.timetable.TimeTableApiService
import lnx.jetitable.api.timetable.data.query.AttendanceData
import lnx.jetitable.api.timetable.data.query.AttendanceListRequest
import lnx.jetitable.api.timetable.data.query.VerifyPresenceRequest
import lnx.jetitable.datastore.UserInfoStore
import lnx.jetitable.misc.DataState
import lnx.jetitable.screens.home.data.ClassUiData
import javax.inject.Inject
import javax.inject.Singleton

interface AttendanceRepository {
    fun getAttendanceList(classData: ClassUiData): Flow<DataState<List<AttendanceData>>>
    fun verifyAttendance(classData: ClassUiData): Flow<DataState<Unit>>
}

@Singleton
class AttendanceRepositoryImpl @Inject constructor(
    private val apiService: TimeTableApiService,
    private val dataStore: UserInfoStore
) : AttendanceRepository {
    override fun getAttendanceList(classData: ClassUiData): Flow<DataState<List<AttendanceData>>> = flow {
        emit(DataState.Loading)

        val studentsAcademyGroupOnly = "1" // TODO: Implement switching

        try {
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
                emit(DataState.Success(data))
            } else {
                emit(DataState.Error(R.string.failed_to_fetch_data))
            }
        } catch (e: Exception) {
            emit(DataState.Error(R.string.failed_to_fetch_data, e))
        }
    }

    override fun verifyAttendance(classData: ClassUiData): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading)

        val userData = dataStore.getApiUserData().first()

        try {
            val response = apiService.get_checkZoom(
                VerifyPresenceRequest(
                    TimeTableApiService.CHECK_ZOOM,
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
                emit(DataState.Success(Unit))
            } else {
                emit(DataState.Error(R.string.failed_to_fetch_data))
            }
        } catch (e: Exception) {
            emit(DataState.Error(R.string.failed_to_fetch_data, e))
        }
    }
}