package lnx.jetitable.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import lnx.jetitable.api.timetable.data.query.ClassNetworkData
import lnx.jetitable.api.timetable.data.query.ExamNetworkData

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("schedule")

class ScheduleDataStore(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun saveClassScheduleList(scheduleList: List<ClassNetworkData>) {
        val jsonString = json.encodeToString(scheduleList)

        context.dataStore.edit {
            it[CLASS_SCHEDULE_LIST] = jsonString
        }
    }

    fun getClassList(): Flow<List<ClassNetworkData>> {
        return context.dataStore.data.map {
            it[CLASS_SCHEDULE_LIST]?.let { encodedList ->
                json.decodeFromString<List<ClassNetworkData>>(encodedList)
            } ?: emptyList()
        }
    }

    suspend fun saveExamScheduleList(examList: List<ExamNetworkData>) {
        val jsonString = json.encodeToString(examList)

        context.dataStore.edit {
            it[EXAM_SCHEDULE_LIST] = jsonString
        }
    }

    fun getExamList(): Flow<List<ExamNetworkData>> {
        return context.dataStore.data.map {
            it[EXAM_SCHEDULE_LIST]?.let { encodedList ->
                json.decodeFromString<List<ExamNetworkData>>(encodedList)
            } ?: emptyList()
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit {
            it.clear()
        }
    }

    companion object {
        val CLASS_SCHEDULE_LIST = stringPreferencesKey("CLASS_SCHEDULE_LIST")
        val EXAM_SCHEDULE_LIST = stringPreferencesKey("EXAM_SCHEDULE_LIST")
    }
}