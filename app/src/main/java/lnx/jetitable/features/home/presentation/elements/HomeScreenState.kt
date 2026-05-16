package lnx.jetitable.features.home.presentation.elements

import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.features.home.domain.models.AttendanceResult
import lnx.jetitable.features.home.domain.models.ScheduleResult
import lnx.jetitable.features.home.presentation.ClassUiData
import lnx.jetitable.features.home.presentation.elements.datepicker.DateState

data class HomeScreenState(
    val classList: ScheduleResult<List<ClassUiData>> = ScheduleResult.Loading,
    val examList: ScheduleResult<List<ExamNetworkData>> = ScheduleResult.Loading,
    val attendanceListState: AttendanceResult = AttendanceResult.Idle,
    val studentFullName: String = "",
    val notifTipState: Boolean = false,
    val dateState: DateState = DateState()
)