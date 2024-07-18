package lnx.jetitable.prefdatastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val API_RESPONSE_DATASTORE = "api_response_datastore"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = API_RESPONSE_DATASTORE)

class DataStoreManager (val context: Context) {
    companion object {
        val TOKEN = stringPreferencesKey("TOKEN")
        val FULL_NAME = stringPreferencesKey("FULL_NAME")
        val GROUP = stringPreferencesKey("GROUP")
        val IS_FULL_TIME = intPreferencesKey("IS_FULL_TIME")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    fun getToken(): Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[TOKEN]
        }


    suspend fun clearDataStore() = context.dataStore.edit {
        it.clear()
    }
}