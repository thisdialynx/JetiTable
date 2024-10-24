package lnx.jetitable.datastore.user

import android.content.Context
import android.util.Log
import lnx.jetitable.datastore.UserDataStore
import lnx.jetitable.misc.getAcademicYear
import lnx.jetitable.misc.getSemester
import lnx.jetitable.timetable.api.ApiService.Companion.CHECK_ACCESS
import lnx.jetitable.timetable.api.RetrofitHolder
import lnx.jetitable.timetable.api.login.data.AccessRequest
import lnx.jetitable.timetable.api.parseAccessResponse

class UserRepository(context: Context) {
    private val userDataStore = UserDataStore(context)
    private val service = RetrofitHolder.getInstance(context)

    suspend fun fetchUserData() {
        try {
            val response = service.checkAccess(
                AccessRequest(CHECK_ACCESS, getSemester().toString(), getAcademicYear())
            )
            val parsedResponse = parseAccessResponse(response)
            userDataStore.saveApiUserData(parsedResponse)
        } catch (e: Exception) {
            Log.e("UserRepo", "Failed to fetch user data", e)
        }
    }
}