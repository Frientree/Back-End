package com.d101.presentation.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.presentation.mypage.event.MyPageViewEvent
import com.d101.presentation.mypage.state.AlarmStatus
import com.d101.presentation.mypage.state.BackgroundMusicStatus
import com.d101.presentation.mypage.state.MyPageViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor() : ViewModel() {

    private val _myPageViewState: MutableStateFlow<MyPageViewState> =
        MutableStateFlow(MyPageViewState.Default())
    val myPageViewState: StateFlow<MyPageViewState> = _myPageViewState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<MyPageViewEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEventOccurred(event: MyPageViewEvent) {
        onReceiveEvent(event)
    }

    // TODO("구현해야 함")
    private fun onReceiveEvent(event: MyPageViewEvent) {
        when (event) {
            MyPageViewEvent.Init -> initViewState()
            MyPageViewEvent.onTapNicknameEditButton -> setEditNicknameState()
            MyPageViewEvent.onCancelNicknameEdit -> rollBackToDefaultState()
            is MyPageViewEvent.onChangeNickname -> {}
            is MyPageViewEvent.onNicknameChanged -> {}
            is MyPageViewEvent.onSetAlarmStatus -> {
                setAlarmStatus(event.alarmStatus)
            }

            is MyPageViewEvent.onSetBackgroundMusicStatus -> {
                setBackgroundMusicStatus()
            }

            MyPageViewEvent.onTapBackgroundMusicChangeButton -> {
                setBackgroundMusicSelectState()
            }

            is MyPageViewEvent.onChangeBackgroundMusic -> {
                changeBackgroundMusic(event.musicName)
            }
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

    private fun setBackgroundMusicSelectState() {
        _myPageViewState.value.let {
            _myPageViewState.value = MyPageViewState.BackgroundMusicSelectState(
                it.id,
                it.nickname,
                it.backgroundMusicStatus,
                it.alarmStatus,
                it.backgroundMusic,
            )
        }
    }

    private fun setEditNicknameState() {
        _myPageViewState.value.let {
            _myPageViewState.value = MyPageViewState.NicknameEditState(
                it.id,
                it.nickname,
                it.backgroundMusicStatus,
                it.alarmStatus,
                it.backgroundMusic,
            )
        }
    }

    private fun rollBackToDefaultState() {
        _myPageViewState.value.let {
            _myPageViewState.value = MyPageViewState.Default(
                it.id,
                it.nickname,
                it.backgroundMusicStatus,
                it.alarmStatus,
                it.backgroundMusic,
            )
        }
    }

    private fun setAlarmStatus(alarmStatus: AlarmStatus) {
        when (val currentState = _myPageViewState.value) {
            is MyPageViewState.Default ->
                _myPageViewState.value =
                    currentState.copy(alarmStatus = alarmStatus)

            is MyPageViewState.NicknameEditState -> {}
            is MyPageViewState.BackgroundMusicSelectState -> {}
        }
    }

    private fun setBackgroundMusicStatus() {
        when (val currentState = _myPageViewState.value) {
            is MyPageViewState.Default -> {
                val newStatus =
                    if (currentState.backgroundMusicStatus == BackgroundMusicStatus.ON) {
                        BackgroundMusicStatus.OFF
                    } else {
                        BackgroundMusicStatus.ON
                    }
                _myPageViewState.value = currentState.copy(backgroundMusicStatus = newStatus)
            }

            is MyPageViewState.NicknameEditState -> {}
            is MyPageViewState.BackgroundMusicSelectState -> {}
        }
    }

    private fun changeBackgroundMusic(newBackgroundMusic: String) {
        _myPageViewState.value.let {
            _myPageViewState.value = MyPageViewState.Default(
                it.id,
                it.nickname,
                it.backgroundMusicStatus,
                it.alarmStatus,
                newBackgroundMusic,
            )
        }
    }

    fun eventOccurred(event: MyPageViewEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }
}
