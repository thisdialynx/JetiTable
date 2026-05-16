package lnx.jetitable.features.about.domain.repository

import lnx.jetitable.features.about.domain.model.Contributor
import lnx.jetitable.features.about.domain.model.ProjectLink

interface AboutRepository {
    fun getContributors(): List<Contributor>
    fun getProjectLinks(): List<ProjectLink>
}