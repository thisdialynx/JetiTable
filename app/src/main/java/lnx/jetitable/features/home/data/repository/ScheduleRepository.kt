package lnx.jetitable.features.home.data.repository

import android.icu.util.Calendar
import android.util.Log
import kotlinx.coroutines.flow.first
import lnx.jetitable.api.timetable.HtmlConverterState
import lnx.jetitable.api.timetable.TimeTableApiService
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.DAILY_CLASS_LIST
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.EXAM_LIST
import lnx.jetitable.api.timetable.data.query.ClassListRequest
import lnx.jetitable.api.timetable.data.query.ClassNetworkData
import lnx.jetitable.api.timetable.data.query.ExamListRequest
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.api.timetable.domain.models.SemesterType
import lnx.jetitable.datastore.ScheduleDataStore
import lnx.jetitable.datastore.UserInfoStore
import lnx.jetitable.features.home.domain.models.ScheduleFetchFailureReason
import lnx.jetitable.features.home.domain.models.ScheduleResult
import lnx.jetitable.features.home.domain.models.ScheduleResult.*
import lnx.jetitable.features.home.domain.repository.ScheduleRepository
import lnx.jetitable.misc.DateHelper
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScheduleRepositoryImpl @Inject constructor(
    private val api: TimeTableApiService,
    private val dataStore: ScheduleDataStore,
    private val dateHelper: DateHelper,
    private val userInfo: UserInfoStore
) : ScheduleRepository {
    val semester = when (dateHelper.getSemester()) {
        SemesterType.AUTUMN -> 1
        SemesterType.SPRING -> 2
        SemesterType.UNKNOWN -> Failure(ScheduleFetchFailureReason.UNKNOWN_ERROR)
    }
    val currentDay = Calendar.getInstance()

    override suspend fun getClasses(calendar: Calendar): ScheduleResult<List<ClassNetworkData>> {

        return try {
            val formattedDate = dateHelper.dateFormat.format(calendar.time)
            val user = userInfo.getUserInfo().first()

            val response = api.get_listLessonTodayStudent(
                ClassListRequest(
                    DAILY_CLASS_LIST,
                    group = user.group,
                    id_group = user.groupId,
                    date = formattedDate,
                    year = dateHelper.getAcademicYears(),
                    semestr = semester.toString(),
                )
            )

            if (response.isSuccessful) {
                when (val body = response.body()) {
                    is HtmlConverterState.Success -> {
                        if (currentDay == dateHelper.selectedDate) dataStore.saveClassScheduleList(
                            body.data
                        )
                        Log.d("Day comparsion result", "${currentDay == dateHelper.selectedDate}")

                        Success(body.data)
                    }

                    HtmlConverterState.Empty -> {
                        if (currentDay == dateHelper.selectedDate) dataStore.saveClassScheduleList(
                            emptyList()
                        )
                        Failure(ScheduleFetchFailureReason.EMPTY)
                    }

                    else -> {
                        Failure(ScheduleFetchFailureReason.UNKNOWN_ERROR)
                    }
                }
            } else {
                Failure(ScheduleFetchFailureReason.NETWORK_ERROR)
            }
        } catch (e: IOException) {
            val cache = dataStore.getClassList().first()

            if (cache.isNotEmpty()) {
                Success(cache)
            } else {
                Failure(ScheduleFetchFailureReason.NO_CACHE)
            }
        } catch (e: Exception) {
            Failure(ScheduleFetchFailureReason.UNKNOWN_ERROR)
        }
    }

    override suspend fun getExams(): ScheduleResult<List<ExamNetworkData>> {
        return try {
            val user = userInfo.getUserInfo().first()
            val response = api.get_sessionStudent(
                ExamListRequest(
                    param = EXAM_LIST,
                    group = user.group,
                    id_group = user.groupId,
                    year = dateHelper.getAcademicYears(),
                    semestr = semester.toString()
                )
            )

            if (response.isSuccessful) {
                when (val body = response.body()) {
                    is HtmlConverterState.Success -> {
                        if (currentDay == dateHelper.selectedDate) dataStore.saveExamScheduleList(
                            body.data
                        )
                        Success(body.data)
                    }

                    HtmlConverterState.Empty -> {
                        if (currentDay == dateHelper.selectedDate) dataStore.saveExamScheduleList(
                            emptyList()
                        )
                        Failure(ScheduleFetchFailureReason.EMPTY)
                    }

                    else -> {
                        Failure(ScheduleFetchFailureReason.UNKNOWN_ERROR)
                    }
                }
            } else {
                Failure(ScheduleFetchFailureReason.NETWORK_ERROR)
            }
        } catch (e: IOException) {
            val cache = dataStore.getExamList().first()

            if (cache.isNotEmpty()) {
                Success(cache)
            } else {
                Failure(ScheduleFetchFailureReason.NO_CACHE)
            }
        } catch (e: Exception) {
            Failure(ScheduleFetchFailureReason.UNKNOWN_ERROR)
        }
    }

    override suspend fun getSavedClasses(): ScheduleResult<List<ClassNetworkData>> {
        return try {
            val classes = dataStore.getClassList().first()

            if (classes.isEmpty()) {
                Failure(ScheduleFetchFailureReason.EMPTY)
            } else {
                Success(classes)
            }
        } catch (e: IOException) {
            Failure(ScheduleFetchFailureReason.IO_ERROR)
        } catch (e: Exception) {
            Failure(ScheduleFetchFailureReason.UNKNOWN_ERROR)
        }
    }

    override suspend fun getSavedExams(): ScheduleResult<List<ExamNetworkData>> {
        return try {
            val exams = dataStore.getExamList().first()

            if (exams.isEmpty()) {
                Failure(ScheduleFetchFailureReason.EMPTY)
            } else {
                Success(exams)
            }
        } catch (e: IOException) {
            Failure(ScheduleFetchFailureReason.IO_ERROR)
        } catch (e: Exception) {
            Failure(ScheduleFetchFailureReason.UNKNOWN_ERROR)
        }
    }
}