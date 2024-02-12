package com.d101.presentation.welcome.event

sealed class FindPasswordEvent {
    data object Init : FindPasswordEvent()
    data object OnFindPasswordAttempt : FindPasswordEvent()
    data object OnReceivedTemporaryPassword : FindPasswordEvent()
    data class OnShowToast(val message: String) : FindPasswordEvent()
}
