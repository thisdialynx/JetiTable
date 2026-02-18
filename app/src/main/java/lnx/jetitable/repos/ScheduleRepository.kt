package lnx.jetitable.repos

import android.icu.util.Calendar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import lnx.jetitable.R
import lnx.jetitable.api.timetable.HtmlConverterState
import lnx.jetitable.api.timetable.TimeTableApiService
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.DAILY_CLASS_LIST
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.EXAM_LIST
import lnx.jetitable.api.timetable.data.login.User
import lnx.jetitable.api.timetable.data.query.ClassListRequest
import lnx.jetitable.api.timetable.data.query.ClassNetworkData
import lnx.jetitable.api.timetable.data.query.ExamListRequest
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.datastore.ScheduleDataStore
import lnx.jetitable.misc.DateManager
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

interface ScheduleRepository {
    fun getClasses(user: User, date: Calendar): Flow<ScheduleState<List<ClassNetworkData>>>
    fun getExams(user: User): Flow<ScheduleState<List<ExamNetworkData>>>

    suspend fun refreshClasses(user: User, date: Calendar): ScheduleState<Any>
    suspend fun refreshExams(user: User): ScheduleState<Any>
}

enum class ScheduleFailureReason(val messageResId: Int) {
    EMPTY(R.string.no_schedule_available_for_today),
    NO_CACHE(R.string.no_cached_schedule),
    UNKNOWN(R.string.failed_to_fetch_data)
}
sealed class ScheduleState<out T> {
    data class Success<T>(val data: List<T>) : ScheduleState<List<T>>()
    data object Loading : ScheduleState<Nothing>()
    data class Failure(
        val reason: ScheduleFailureReason,
        val exception: Throwable? = null
    ) : ScheduleState<Nothing>()
}

@Singleton
class ScheduleRepositoryImpl @Inject constructor(
    private val api: TimeTableApiService,
    private val dataStore: ScheduleDataStore,
    private val dateManager: DateManager
) : ScheduleRepository {
    override fun getClasses(
        user: User,
        date: Calendar
    ): Flow<ScheduleState<List<ClassNetworkData>>> = flow {
        emit(ScheduleState.Loading)

        val cachedData = dataStore.getClassList().first()

        if (cachedData.isNotEmpty()) emit(ScheduleState.Success(cachedData))

        try {
            val formattedDate = dateManager.dateFormat.format(date.time)
            val response = api.get_listLessonTodayStudent(
                ClassListRequest(
                    DAILY_CLASS_LIST,
                    group = user.group,
                    id_group = user.groupId,
                    date = formattedDate,
                    year = dateManager.getAcademicYears(),
                    semestr = dateManager.getSemester().toString(),
                )
            )

            if (response == HtmlConverterState.Empty) {
                emit(ScheduleState.Failure(ScheduleFailureReason.EMPTY))
            } else {
                val data = (response as HtmlConverterState.Success).data
                dataStore.saveClassScheduleList(data)
                emit(ScheduleState.Success(data))
            }
        } catch (e: Exception) {
            if (cachedData.isNotEmpty()) {
                emit(ScheduleState.Success(cachedData))
            } else {
                if (e is UnknownHostException || e is IOException) {
                    emit(ScheduleState.Failure(ScheduleFailureReason.UNKNOWN, e))
                } else {
                    emit(ScheduleState.Failure(ScheduleFailureReason.NO_CACHE, e))
                }
            }
        }
    }

    override fun getExams(user: User): Flow<ScheduleState<List<ExamNetworkData>>> = flow {
        emit(ScheduleState.Loading)

        val cachedData = dataStore.getExamList().first()

        if (cachedData.isNotEmpty()) emit(ScheduleState.Success(cachedData))

        try {
            val response = api.get_sessionStudent(
                ExamListRequest(
                    param = EXAM_LIST,
                    group = user.group,
                    id_group = user.groupId,
                    year = dateManager.getAcademicYears(),
                    semestr = dateManager.getSemester().toString()
                )
            )

            if (response == HtmlConverterState.Empty) {
                emit(ScheduleState.Failure(ScheduleFailureReason.EMPTY))
            } else {
                val data = (response as HtmlConverterState.Success).data
                dataStore.saveExamScheduleList(data)
                emit(ScheduleState.Success(data))
            }
        } catch (e: Exception) {
            if (cachedData.isNotEmpty()) {
                emit(ScheduleState.Success(cachedData))
            } else {
                if (e is UnknownHostException || e is IOException) {
                    emit(ScheduleState.Failure(ScheduleFailureReason.UNKNOWN, e))
                } else {
                    emit(ScheduleState.Failure(ScheduleFailureReason.NO_CACHE, e))
                }
            }
        }
    }

    override suspend fun refreshClasses(user: User, date: Calendar): ScheduleState<Any> {
        return try {
            val formattedDate = dateManager.dateFormat.format(date.time)

            when (val response = api.get_listLessonTodayStudent(
                ClassListRequest(
                    DAILY_CLASS_LIST,
                    group = user.group,
                    id_group = user.groupId,
                    date = formattedDate,
                    year = dateManager.getAcademicYears(),
                    semestr = dateManager.getSemester().toString(),
                )
            )) {
                is HtmlConverterState.Success -> {
                    dataStore.saveClassScheduleList(response.data)
                    ScheduleState.Success(response.data)
                }
                is HtmlConverterState.Empty -> {
                    dataStore.saveClassScheduleList(emptyList())
                    ScheduleState.Failure(ScheduleFailureReason.EMPTY)
                }
                is HtmlConverterState.Failure -> {
                    ScheduleState.Failure(ScheduleFailureReason.UNKNOWN, response.exception)
                }
                is HtmlConverterState.Loading -> ScheduleState.Loading
            }
        } catch (e: Exception) {
            ScheduleState.Failure(ScheduleFailureReason.UNKNOWN, e)
        }
    }

    override suspend fun refreshExams(user: User): ScheduleState<Any> {
        return try {
            when (val response = api.get_sessionStudent(
                ExamListRequest(
                    param = EXAM_LIST,
                    group = user.group,
                    id_group = user.groupId,
                    year = dateManager.getAcademicYears(),
                    semestr = dateManager.getSemester().toString()
                )
            )) {
                is HtmlConverterState.Success -> {
                    dataStore.saveExamScheduleList(response.data)
                    ScheduleState.Success(response.data)
                }
                is HtmlConverterState.Empty -> {
                    dataStore.saveExamScheduleList(emptyList())
                    ScheduleState.Failure(ScheduleFailureReason.EMPTY)
                }
                is HtmlConverterState.Failure -> {
                    ScheduleState.Failure(ScheduleFailureReason.UNKNOWN, response.exception)
                }
                is HtmlConverterState.Loading -> ScheduleState.Loading
            }
        } catch (e: Exception) {
            ScheduleState.Failure(ScheduleFailureReason.UNKNOWN, e)
        }
    }
}