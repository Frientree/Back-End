package com.d101.presentation.mypage.state

sealed class MyPageViewState {
    abstract val isSocial: Boolean
    abstract val id: String
    abstract val nickname: String
    abstract val alarmStatus: AlarmStatus
    abstract val backgroundMusicStatus: BackgroundMusicStatus
    abstract val backgroundMusic: String

    data class Default(
        override val isSocial: Boolean = false,
        override val id: String = "",
        override val nickname: String = "",
        override val backgroundMusicStatus: BackgroundMusicStatus = BackgroundMusicStatus.ON,
        override val alarmStatus: AlarmStatus = AlarmStatus.ON,
        override val backgroundMusic: String = "",
    ) : MyPageViewState()

    data class NicknameEditState(
        override val isSocial: Boolean,
        override val id: String,
        override val nickname: String,
        override val backgroundMusicStatus: BackgroundMusicStatus,
        override val alarmStatus: AlarmStatus,
        override val backgroundMusic: String,
    ) : MyPageViewState()

    data class BackgroundMusicSelectState(
        override val isSocial: Boolean = false,
        override val id: String,
        override val nickname: String,
        override val backgroundMusicStatus: BackgroundMusicStatus,
        override val alarmStatus: AlarmStatus,
        override val backgroundMusic: String,
    ) : MyPageViewState()
}
