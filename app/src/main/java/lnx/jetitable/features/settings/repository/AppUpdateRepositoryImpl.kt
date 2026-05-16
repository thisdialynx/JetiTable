package lnx.jetitable.features.settings.repository

import lnx.jetitable.api.github.GithubApiService
import lnx.jetitable.features.settings.domain.model.AppUpdateState
import lnx.jetitable.features.settings.domain.model.UpdateCheckError
import lnx.jetitable.features.settings.domain.model.UpdateResult
import lnx.jetitable.features.settings.domain.repository.AppUpdateRepository
import okio.IOException
import javax.inject.Inject

class AppUpdateRepositoryImpl @Inject constructor(
    private val api: GithubApiService
) : AppUpdateRepository {
    override suspend fun checkForUpdates(currentVersion: String): UpdateResult {
        return try {
            val release = api.getLatestRelease()
            val latestVersion = release.tag_name.removePrefix("v")
            val updateAvailable = isNewerVersion(latestVersion, currentVersion)
            val downloadUrl = release.assets[1].browser_download_url

            if (updateAvailable) {
                UpdateResult.Available(
                    AppUpdateState(
                        currentVersion,
                        latestVersion,
                        updateAvailable,
                        downloadUrl,
                        release.body
                    )
                )
            } else UpdateResult.Latest
        } catch (e: IOException) {
            UpdateResult.Failure(UpdateCheckError.NETWORK_ERROR)
        } catch (e: Exception) {
            UpdateResult.Failure(UpdateCheckError.UNKNOWN_ERROR)
        }
    }

    private fun isNewerVersion(latestVersion: String, currentVersion: String): Boolean {
        val latest = latestVersion.split(".").map { it.toIntOrNull() ?: 0 }
        val current = currentVersion.split(".").map { it.toIntOrNull() ?: 0 }

        for (i in 0 until minOf(latest.size, current.size)) {
            if (latest[i] > current[i]) return true
            if (latest[i] < current[i]) return false
        }
        return latest.size > current.size
    }
}