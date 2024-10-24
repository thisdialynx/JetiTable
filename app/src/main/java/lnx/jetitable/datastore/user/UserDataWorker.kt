package lnx.jetitable.datastore.user

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class UserDataWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val userRepository = UserRepository(applicationContext)

        return try {
            userRepository.fetchUserData()
            Result.success()
        } catch (e: Exception) {
            Log.e("UserRepo", "Failed to fetch user data", e)
            Result.failure()
        }
    }
}