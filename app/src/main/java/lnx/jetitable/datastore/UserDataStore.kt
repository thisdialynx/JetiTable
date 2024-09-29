package lnx.jetitable.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import lnx.jetitable.timetable.api.login.data.User


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data_store")

class UserDataStore (private val context: Context) {

    suspend fun saveApiUserData(user: User) {
        context.dataStore.edit {
            it[FULL_NAME] = user.fio
            it[FULL_NAME_ID] = user.id_fio
            it[GROUP] = user.group
            it[GROUP_ID] = user.id_group
            it[IS_FULL_TIME] = user.denne
            it[STATUS] = user.status
            it[USER_ID] = user.id_user
        }
    }

    suspend fun getApiUserData(): User {
        val preferences = context.dataStore.data.first()
        return User(
            fio = preferences[FULL_NAME] ?: "",
            id_fio = preferences[FULL_NAME_ID] ?: "",
            group = preferences[GROUP] ?: "",
            id_group = preferences[GROUP_ID] ?: "",
            denne = preferences[IS_FULL_TIME] ?: 0,
            status = preferences[STATUS] ?: "",
            id_user = preferences[USER_ID] ?: ""
        )
    }

    suspend fun clearDataStore() = context.dataStore.edit {
        it.clear()
    }

    companion object {
        val FULL_NAME = stringPreferencesKey("FULL_NAME")
        val FULL_NAME_ID = stringPreferencesKey("FULL_NAME_ID")
        val GROUP = stringPreferencesKey("GROUP")
        val GROUP_ID = stringPreferencesKey("GROUP_ID")
        val IS_FULL_TIME = intPreferencesKey("IS_FULL_TIME")
        val STATUS = stringPreferencesKey("STATUS")
        val USER_ID = stringPreferencesKey("USER_ID")
    }
}