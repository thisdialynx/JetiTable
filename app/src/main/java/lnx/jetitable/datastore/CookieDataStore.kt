package lnx.jetitable.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("cookies")

class CookieDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) : CookieJar {
    private val dataStore = context.dataStore

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val key = stringPreferencesKey(url.toString())
        val cookieString = cookies.joinToString(";") { it.toString() }

        runBlocking {
            dataStore.edit {
                it[key] = cookieString
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val key = stringPreferencesKey(url.toString())
        val cookies: Flow<String?> = dataStore.data.map {
            it[key]
        }

        val cookieString = runBlocking {
            cookies.first()
        }

        return cookieString?.split(";")?.mapNotNull { Cookie.parse(url, it) } ?: emptyList()
    }

    fun clearCookies() {
        runBlocking {
            dataStore.edit {
                it.clear()
            }
        }
    }
}