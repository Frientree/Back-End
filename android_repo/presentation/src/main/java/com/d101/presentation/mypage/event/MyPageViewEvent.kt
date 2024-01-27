package com.d101.presentation.mypage.event

import com.d101.presentation.mypage.state.AlarmStatus

sealed class MyPageViewEvent {

    data object onTapNicknameEditButton : MyPageViewEvent()

    data object onCancelNicknameEdit : MyPageViewEvent()

    data class onChangeNickname(val nicknameInput: String) : MyPageViewEvent()

    data class onNicknameChanged(val newNickname: String) : MyPageViewEvent()

    data class onSetAlarmStatus(val alarmStatus: AlarmStatus) : MyPageViewEvent()

    data object onSetBackgroundMusicStatus : MyPageViewEvent()

    data class onChangeBackgroundMusic(val musicName: String) : MyPageViewEvent()

    data object onTapTermsButton : MyPageViewEvent()

    data class onShowTerms(val termsUrl: String) : MyPageViewEvent()

    data object onTapChangePasswordButton : MyPageViewEvent()

    data object onTapLogOutButton : MyPageViewEvent()

    data object Init : MyPageViewEvent()
}
