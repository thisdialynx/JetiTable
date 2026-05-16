package lnx.jetitable.features.about.data.repository

import lnx.jetitable.features.about.domain.model.Contributor
import lnx.jetitable.features.about.domain.model.ContributorRole
import lnx.jetitable.features.about.domain.model.PlatformType
import lnx.jetitable.features.about.domain.model.ProjectLink
import lnx.jetitable.features.about.domain.repository.AboutRepository
import javax.inject.Inject

class AboutRepositoryImpl @Inject constructor() : AboutRepository {

    override fun getContributors(): List<Contributor> {
        return listOf(
            Contributor(
                name = "Dialynx",
                role = ContributorRole.APP_DEVELOPER,
                profilePictureUrl = "https://github.com/thisdialynx.png"
            ),
            Contributor(
                name = "Denys Ratov",
                role = ContributorRole.TIMETABLE_DEVELOPER,
                profilePictureUrl = null
            )
        )
    }

    override fun getProjectLinks(): List<ProjectLink> {
        return listOf(
            ProjectLink("github.com/thisdialynx/JetiTable", PlatformType.GITHUB),
            ProjectLink("snu.edu.ua", PlatformType.UNIVERSITY),
            ProjectLink("timetable.lond.lg.ua", PlatformType.WEBSITE)
        )
    }
}