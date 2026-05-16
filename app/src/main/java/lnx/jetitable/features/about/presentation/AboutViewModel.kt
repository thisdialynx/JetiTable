package lnx.jetitable.features.about.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import lnx.jetitable.features.about.domain.usecase.GetAboutInfoUseCase
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val getAboutInfoUseCase: GetAboutInfoUseCase
) : ViewModel() {

    private val _aboutState = MutableStateFlow<AboutScreenState?>(null)
    val aboutState: StateFlow<AboutScreenState?> = _aboutState.asStateFlow()

    init {
        loadAboutInfo()
    }

    private fun loadAboutInfo() {
        val domainData = getAboutInfoUseCase()

        val uiContributors = domainData.contributors.map { it.toUi() }
        val uiProjectLinks = domainData.links.map { it.toUi() }

        val state = AboutScreenState(uiContributors, uiProjectLinks)

        _aboutState.value = state
    }
}