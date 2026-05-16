package lnx.jetitable.features.about.presentation

data class AboutScreenState(
    val contributors: List<ContributorUi>,
    val links: List<ProjectLinkUi>
)
