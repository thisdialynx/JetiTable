package lnx.jetitable.features.about.domain.model

enum class ContributorRole {
    APP_DEVELOPER,
    TIMETABLE_DEVELOPER
}

enum class PlatformType {
    GITHUB,
    WEBSITE,
    UNIVERSITY
}

data class Contributor(
    val name: String,
    val role: ContributorRole,
    val profilePictureUrl: String?
)

data class ProjectLink(
    val url: String,
    val platform: PlatformType
)