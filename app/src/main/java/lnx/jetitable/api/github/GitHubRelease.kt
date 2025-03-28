package lnx.jetitable.api.github

data class GitHubRelease(
    val tag_name: String,
    val body: String,
    val assets: List<GitHubAsset>
)

data class GitHubAsset(
    val browser_download_url: String,
    val name: String,
    val size: Long
)
