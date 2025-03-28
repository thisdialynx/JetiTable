package lnx.jetitable.api.github

import retrofit2.http.GET

interface GithubApiService {
    @GET("repos/thisdialynx/JetiTable/releases/latest")
    suspend fun getLatestRelease(): GitHubRelease
}