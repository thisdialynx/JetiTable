package lnx.jetitable.core

import android.content.Context

class UserSettings (private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("UserSettings", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString("authToken", token).apply()
    }

    fun getAuthToken(): String? = sharedPreferences.getString("authToken", null)
}