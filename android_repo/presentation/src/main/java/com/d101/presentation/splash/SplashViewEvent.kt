package com.d101.presentation.splash

sealed class SplashViewEvent {

    data object ShowSplash : SplashViewEvent()
    data object AutoSignInSuccess : SplashViewEvent()
    data object AutoSignInFailure : SplashViewEvent()
}
