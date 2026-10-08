package lnx.jetitable.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
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
        val key = stringPreferencesKey(url.host)

        runBlocking {
            val existingCookies = loadForRequest(url).toMutableList()

            for (newCookie in cookies) {
                existingCookies.removeAll { it.name == newCookie.name }
                existingCookies.add(newCookie)
            }

            val cookieLines = existingCookies.joinToString("\n") { it.toString() }

            dataStore.edit {
                it[key] = cookieLines
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val key = stringPreferencesKey(url.host)
        val cookies = runBlocking {
            dataStore.data.map { it[key] }.first()
        }

        if (cookies.isNullOrEmpty()) return emptyList()

        return cookies.split("\n").mapNotNull { Cookie.parse(url, it) }
    }

    fun clearCookies() {
        runBlocking {
            dataStore.edit {
                it.clear()
            }
        }
    }
}