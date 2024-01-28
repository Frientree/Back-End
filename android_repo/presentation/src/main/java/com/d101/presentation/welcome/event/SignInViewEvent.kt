package com.d101.presentation.welcome.event

sealed class SignInViewEvent {
    data object OnSignInViewByFrientree : SignInViewEvent()
    data object OnSignInViewByKakao : SignInViewEvent()
    data object OnNavigateToTermsAgree : SignInViewEvent()
    data object OnNavigateToFindPassword : SignInViewEvent()
    data object OnNavigateToMain : SignInViewEvent()
}
