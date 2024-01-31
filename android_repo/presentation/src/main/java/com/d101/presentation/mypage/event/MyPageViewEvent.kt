package com.d101.presentation.mypage.event

import com.d101.presentation.mypage.state.AlarmStatus

sealed class MyPageViewEvent {

    data object OnTapNicknameEditButton : MyPageViewEvent()

    data object OnTapNicknameEditCancelButton : MyPageViewEvent()

    data class OnChangeNickname(val nicknameInput: String) : MyPageViewEvent()

    data class OnNicknameChanged(val newNickname: String) : MyPageViewEvent()

    data class OnTapAlarmStatusButton(val alarmStatus: AlarmStatus) : MyPageViewEvent()

    data object OnTapBackgroundMusicStatusButton : MyPageViewEvent()

    data object OnTapBackgroundMusicChangeButton : MyPageViewEvent()

    data class OnBackgroundMusicChanged(val musicName: String) : MyPageViewEvent()

    data object OnTapTermsButton : MyPageViewEvent()

    data object OnTapChangePasswordButton : MyPageViewEvent()

    data object OnTapLogOutButton : MyPageViewEvent()

    data object Init : MyPageViewEvent()
}
