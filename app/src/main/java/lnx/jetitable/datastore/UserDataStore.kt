package lnx.jetitable.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import lnx.jetitable.timetable.api.login.data.AccessResponse
import lnx.jetitable.timetable.api.login.data.User


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data_store")

class UserDataStore (private val context: Context) {

    suspend fun saveUserData(response: AccessResponse) {
        context.dataStore.edit {
            it[FULL_NAME] = response.user.fio
            it[FULL_NAME_ID] = response.user.id_fio
            it[GROUP] = response.user.group
            it[GROUP_ID] = response.user.id_group
            it[IS_FULL_TIME] = response.user.denne
            it[STATUS] = response.user.status
            it[USER_ID] = response.user.id_user
            it[KEY] = response.user.key
            it[FACULTY_CODE] = response.user.kod_faculty
        }
    }

    fun getUserData(): Flow<User> {
        return context.dataStore.data.map {
            User(
                fio = it[FULL_NAME] ?: "",
                id_fio = it[FULL_NAME_ID] ?: 0,
                group = it[GROUP] ?: "",
                id_group = it[GROUP_ID] ?: "",
                denne = it[IS_FULL_TIME] ?: 0,
                status = it[STATUS] ?: "",
                id_user = it[USER_ID] ?: 0,
                key = it[KEY] ?: "",
                kod_faculty = it[FACULTY_CODE] ?: 0
            )
        }
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