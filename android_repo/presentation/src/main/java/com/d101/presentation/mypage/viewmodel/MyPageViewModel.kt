package com.d101.presentation.mypage.viewmodel

import androidx.lifecycle.ViewModel
import com.d101.presentation.mypage.event.MyPageViewEvent
import com.d101.presentation.mypage.state.MyPageViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor() : ViewModel() {

    private val _myPageViewState: MutableStateFlow<MyPageViewState> =
        MutableStateFlow(MyPageViewState.Default())
    val myPageViewState: StateFlow<MyPageViewState> = _myPageViewState.asStateFlow()

    val event = MutableSharedFlow<MyPageViewEvent>()

    fun onEventOccurred(event: MyPageViewEvent) {
        onReceiveEvent(event)
    }

// TODO("구현해야 함")
    private fun onReceiveEvent(event: MyPageViewEvent) {
        when (event) {
            MyPageViewEvent.Init -> initViewState()
            is MyPageViewEvent.OnChangeNickname -> {}
            is MyPageViewEvent.onChangeBackgroundMusic -> {}
            is MyPageViewEvent.onChangeNickname -> {}
            is MyPageViewEvent.onNicknameChanged -> {}
            is MyPageViewEvent.onSetAlarmStatus -> {}
            is MyPageViewEvent.onShowTerms -> {}
            MyPageViewEvent.onTapChangePasswordButton -> {}
            MyPageViewEvent.onTapLogOutButton -> {}
            MyPageViewEvent.onTapTermsButton -> {}
        }
    }

// TODO(" 서버에서 유저 정보 가져오는 것으로 수정해야 함")
    private fun initViewState() {
        _myPageViewState.value = MyPageViewState.Default(id = "테스트1")
    }
}
