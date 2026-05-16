package lnx.jetitable.features.home.domain.usecase

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import lnx.jetitable.services.data.DataSyncService
import lnx.jetitable.services.data.UserDataWorker
import javax.inject.Inject

class EnqueueSyncRequestUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        val syncRequest = OneTimeWorkRequestBuilder<UserDataWorker>()
            .addTag(DataSyncService.DATA_SYNC_SERVICE_NAME)
            .build()

        WorkManager.getInstance(context).enqueue(syncRequest)
    }
}