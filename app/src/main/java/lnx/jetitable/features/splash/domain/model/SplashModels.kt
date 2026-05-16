package lnx.jetitable.features.splash.domain.model

sealed class SplashState {
    object Loading : SplashState()
    object Authorized : SplashState()
    object Unauthorized : SplashState()
}