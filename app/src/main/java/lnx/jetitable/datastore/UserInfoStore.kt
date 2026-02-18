package lnx.jetitable.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lnx.jetitable.api.timetable.data.login.AccessResponse
import lnx.jetitable.api.timetable.data.login.User
import javax.inject.Inject


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data_store")

class UserInfoStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun saveApiUserData(response: AccessResponse) {
        context.dataStore.edit {
            it[FULL_NAME] = response.user.fullName
            it[FULL_NAME_ID] = response.user.fullNameId
            it[GROUP] = response.user.group
            it[GROUP_ID] = response.user.groupId
            it[IS_FULL_TIME] = response.user.isFullTime
            it[STATUS] = response.user.status
            it[USER_ID] = response.user.userId
            it[KEY] = response.user.key
            it[FACULTY_CODE] = response.user.facultyCode
        }
    }

    fun getApiUserData(): Flow<User> {
        return context.dataStore.data.map {
            User(
                fullName = it[FULL_NAME] ?: "",
                fullNameId = it[FULL_NAME_ID] ?: 0,
                group = it[GROUP] ?: "",
                groupId = it[GROUP_ID] ?: "",
                isFullTime = it[IS_FULL_TIME] ?: 0,
                status = it[STATUS] ?: "",
                userId = it[USER_ID] ?: 0,
                key = it[KEY] ?: "",
                facultyCode = it[FACULTY_CODE] ?: 0
            )
        }
    }

    suspend fun clearAll() = context.dataStore.edit { it.clear() }

    companion object {
        val FULL_NAME = stringPreferencesKey("FULL_NAME")
        val FULL_NAME_ID = intPreferencesKey("FULL_NAME_ID")
        val GROUP = stringPreferencesKey("GROUP")
        val GROUP_ID = stringPreferencesKey("GROUP_ID")
        val IS_FULL_TIME = intPreferencesKey("IS_FULL_TIME")
        val STATUS = stringPreferencesKey("STATUS")
        val USER_ID = intPreferencesKey("USER_ID")
        val KEY = stringPreferencesKey("KEY")
        val FACULTY_CODE = intPreferencesKey("KOD_FACULTY")
    }
}