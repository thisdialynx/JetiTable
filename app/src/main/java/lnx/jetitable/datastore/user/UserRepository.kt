package lnx.jetitable.datastore.user

import android.content.Context
import android.util.Log
import lnx.jetitable.datastore.UserDataStore
import lnx.jetitable.misc.DateManager
import lnx.jetitable.api.timetable.TimeTableApiService.Companion.CHECK_ACCESS
import lnx.jetitable.api.RetrofitHolder
import lnx.jetitable.api.timetable.data.login.AccessRequest

class UserRepository(context: Context) {
    private val userDataStore = UserDataStore(context)
    private val service = RetrofitHolder.getTimeTableApiInstance(context)
    private val dateManager = DateManager()

    suspend fun fetchUserData() {
        try {
            val response = service.checkAccess(
                AccessRequest(CHECK_ACCESS, dateManager.getSemester().toString(), dateManager.getAcademicYears())
            )
            userDataStore.saveUserData(response)
        } catch (e: Exception) {
            Log.e("UserRepo", "Failed to fetch user data", e)
        }
    }
}