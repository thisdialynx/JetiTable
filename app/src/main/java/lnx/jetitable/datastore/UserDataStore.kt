package lnx.jetitable.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import lnx.jetitable.timetable.api.login.data.AccessResponse
import lnx.jetitable.timetable.api.login.data.User


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data_store")

class UserDataStore (private val context: Context) {

    suspend fun saveApiUserData(accessResponse: AccessResponse) {
        context.dataStore.edit {
            it[FULL_NAME] = accessResponse.user.fio
            it[FULL_NAME_ID] = accessResponse.user.id_fio
            it[GROUP] = accessResponse.user.group
            it[GROUP_ID] = accessResponse.user.id_group
            it[IS_FULL_TIME] = accessResponse.user.denne
            it[STATUS] = accessResponse.user.status
            it[USER_ID] = accessResponse.user.id_user
            it[KEY] = accessResponse.user.key
            it[FACULTY_CODE] = accessResponse.user.kod_faculty
        }
    }

    suspend fun getApiUserData(): User {
        val preferences = context.dataStore.data.first()
        return User(
            fio = preferences[FULL_NAME] ?: "",
            id_fio = preferences[FULL_NAME_ID] ?: 0,
            group = preferences[GROUP] ?: "",
            id_group = preferences[GROUP_ID] ?: "",
            denne = preferences[IS_FULL_TIME] ?: 0,
            status = preferences[STATUS] ?: "",
            id_user = preferences[USER_ID] ?: 0,
            key = preferences[KEY] ?: "",
            kod_faculty = preferences[FACULTY_CODE] ?: 0
        )
    }

    suspend fun clearDataStore() = context.dataStore.edit { it.clear() }

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