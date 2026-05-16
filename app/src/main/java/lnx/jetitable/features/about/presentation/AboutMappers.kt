package lnx.jetitable.features.about.presentation

import androidx.compose.ui.graphics.vector.ImageVector
import lnx.jetitable.R
import lnx.jetitable.features.about.domain.model.Contributor
import lnx.jetitable.features.about.domain.model.ContributorRole
import lnx.jetitable.features.about.domain.model.PlatformType
import lnx.jetitable.features.about.domain.model.ProjectLink
import lnx.jetitable.ui.icons.Github
import lnx.jetitable.ui.icons.Snu
import lnx.jetitable.ui.icons.google.CalendarMonth

data class ProjectLinkUi(
    val url: String,
    val platformIcon: ImageVector,
    val titleResId: Int,
)

data class ContributorUi(
    val name: String,
    val profilePictureUrl: String?,
    val roleResId: Int
)

fun ProjectLink.toUi(): ProjectLinkUi {
    val icon = when (platform) {
        PlatformType.GITHUB -> Github
        PlatformType.UNIVERSITY -> Snu
        PlatformType.WEBSITE -> CalendarMonth
    }

    val title = when (platform) {
        PlatformType.GITHUB -> R.string.github
        PlatformType.UNIVERSITY -> R.string.university
        PlatformType.WEBSITE -> R.string.timetable
    }

    return ProjectLinkUi(url, icon, title)
}

fun Contributor.toUi(): ContributorUi {
    val role = when (role) {
        ContributorRole.APP_DEVELOPER -> R.string.developer
        ContributorRole.TIMETABLE_DEVELOPER -> R.string.timetable_developer
    }

    return ContributorUi(name, profilePictureUrl, role)
}
