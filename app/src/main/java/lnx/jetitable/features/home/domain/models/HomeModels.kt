package lnx.jetitable.features.home.domain.models

import lnx.jetitable.api.timetable.data.query.AttendanceData
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.features.home.presentation.ClassUiData

enum class ScheduleFetchFailureReason {
    EMPTY,
    UNKNOWN_ERROR,
    NETWORK_ERROR,
    IO_ERROR,
    NO_CACHE
}

enum class AttendanceFailureReason {
    NETWORK_ERROR,
    UNKNOWN_ERROR
}

sealed class AttendanceVerificationResult {
    object Success : AttendanceVerificationResult()
    data class Failure(val error: AttendanceFailureReason) : AttendanceVerificationResult()
}

sealed class AttendanceResult {
    data class Failure(val error: AttendanceFailureReason) : AttendanceResult()
    data class Success(val data: List<AttendanceData>) : AttendanceResult()
    object Idle : AttendanceResult()
}

sealed class ScheduleResult<out T> {
    data class Success<T>(val data: List<T>) : ScheduleResult<List<T>>()
    data object Loading : ScheduleResult<Nothing>()
    data class Failure(val error: ScheduleFetchFailureReason) : ScheduleResult<Nothing>()
}

data class ScheduleState(
    val classList: ScheduleResult<List<ClassUiData>>,
    val examList: ScheduleResult<List<ExamNetworkData>>
)