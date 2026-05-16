package lnx.jetitable.features.splash.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lnx.jetitable.features.splash.domain.model.SplashState

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onAuthNavigate: () -> Unit,
    onHomeNavigate: () -> Unit
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(screenState) {
        when (screenState) {
            SplashState.Loading -> Unit
            SplashState.Authorized -> onHomeNavigate()
            SplashState.Unauthorized -> onAuthNavigate()
        }
    }

    SplashLayout()
}

@Preview
@Composable
fun SplashPreview() {
    SplashLayout()
}