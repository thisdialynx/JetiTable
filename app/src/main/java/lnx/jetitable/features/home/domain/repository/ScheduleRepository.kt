package lnx.jetitable.features.home.domain.repository

import android.icu.util.Calendar
import lnx.jetitable.api.timetable.data.query.ClassNetworkData
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.features.home.domain.models.ScheduleResult

interface ScheduleRepository {
    suspend fun getClasses(calendar: Calendar): ScheduleResult<List<ClassNetworkData>>
    suspend fun getExams(): ScheduleResult<List<ExamNetworkData>>
}