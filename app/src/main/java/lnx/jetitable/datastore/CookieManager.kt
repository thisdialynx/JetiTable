package lnx.jetitable.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

private val Context.dataStore by preferencesDataStore(name = "cookies")

class CookieManager(context: Context) : CookieJar {
    private val dataStore = context.dataStore

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val key = stringPreferencesKey(url.toString())
        val cookieString = cookies.joinToString(";") { it.toString() }

        runBlocking {
            dataStore.edit { preferences ->
                preferences[key] = cookieString
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val key = stringPreferencesKey(url.toString())
        val cookies: Flow<String?> = dataStore.data.map { preferences ->
            preferences[key]
        }

        val cookieString = runBlocking {
            cookies.first()
        }

        return cookieString?.split(";")?.mapNotNull { Cookie.parse(url, it) } ?: emptyList()
    }
    fun clearCookies() {
        runBlocking {
            dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }
}