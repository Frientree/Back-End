package com.d101.presentation.splash

sealed class SplashViewEvent {

    data object ShowSplash : SplashViewEvent()
    data object AutoSignInSuccess : SplashViewEvent()
    data object AutoSignInFailure : SplashViewEvent()
    data class CheckAppStatus(
        val appAvailable: Boolean,
        val minVersion: String,
        val storeUrl: String,
    ) : SplashViewEvent()
    data object OnFailCheckAppStatus : SplashViewEvent()
}
