package lnx.jetitable.api.github

import retrofit2.http.GET

interface GithubApiService {
    @GET("repos/thisdialynx/JetiTable/releases/latest")
    suspend fun getLatestRelease(): GitHubRelease

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}