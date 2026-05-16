package lnx.jetitable.features.about.domain.usecase

import lnx.jetitable.features.about.domain.model.Contributor
import lnx.jetitable.features.about.domain.model.ProjectLink
import lnx.jetitable.features.about.domain.repository.AboutRepository
import javax.inject.Inject

data class AboutInfo(
    val contributors: List<Contributor>,
    val links: List<ProjectLink>
)

class GetAboutInfoUseCase @Inject constructor(
    private val repository: AboutRepository
) {
    operator fun invoke(): AboutInfo {
        val contributors = repository.getContributors()
        val links = repository.getProjectLinks()

        return AboutInfo(contributors, links)
    }
}