package com.d101.presentation.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Result
import com.d101.domain.usecase.usermanagement.GetUserInfoUseCase
import com.d101.presentation.mypage.event.MyPageViewEvent
import com.d101.presentation.mypage.state.AlarmStatus
import com.d101.presentation.mypage.state.BackgroundMusicStatus
import com.d101.presentation.mypage.state.MyPageViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<MyPageViewState> =
        MutableStateFlow(MyPageViewState.Default())
    val uiState: StateFlow<MyPageViewState> = _uiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<MyPageViewEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    init {
        viewModelScope.launch { _eventFlow.emit(MyPageViewEvent.Init) }
    }

    fun onTapNicknameEditButton() {
        viewModelScope.launch { _eventFlow.emit(MyPageViewEvent.OnTapNicknameEditButton) }
    }

    fun onTapNicknameEditCancelButton() {
        viewModelScope.launch { _eventFlow.emit(MyPageViewEvent.OnTapNicknameEditCancelButton) }
    }

    fun onChangeNickname(nicknameInput: String) {
        viewModelScope.launch { _eventFlow.emit(MyPageViewEvent.OnChangeNickname(nicknameInput)) }
    }

    fun onNicknameChanged(newNickname: String) {
        viewModelScope.launch { _eventFlow.emit(MyPageViewEvent.OnNicknameChanged(newNickname)) }
    }

    fun onTapAlarmStatusButton(alarmStatus: AlarmStatus) {
        viewModelScope.launch {
            _eventFlow.emit(MyPageViewEvent.OnTapAlarmStatusButton(alarmStatus))
        }
    }

    fun onTapBackgroundMusicStatusButton() {
        viewModelScope.launch { _eventFlow.emit(MyPageViewEvent.OnTapBackgroundMusicStatusButton) }
    }

    fun onTapBackgroundMusicChangeButton() {
        viewModelScope.launch { _eventFlow.emit(MyPageViewEvent.OnTapBackgroundMusicChangeButton) }
    }

    fun onBackgroundMusicChanged(musicName: String) {
        viewModelScope.launch {
            _eventFlow.emit(MyPageViewEvent.OnBackgroundMusicChanged(musicName))
        }
    }

    fun onTapTermsButton() {
        viewModelScope.launch { _eventFlow.emit(MyPageViewEvent.OnTapTermsButton) }
    }

    fun onTapChangePasswordButton() {
        viewModelScope.launch { _eventFlow.emit(MyPageViewEvent.OnTapChangePasswordButton) }
    }

    fun onTapLogOutButton() {
        viewModelScope.launch { _eventFlow.emit(MyPageViewEvent.OnTapLogOutButton) }
    }

    fun onTapNicknameEditButtonOccurred() {
        setEditNicknameState()
    }

    fun onTapNicknameEditCancelButtonOccurred() {
        setDefaultState()
    }

    fun onChangeNicknameOccurred(nicknameInput: String) {
    }

    fun onNicknameChangedOccurred(newNickname: String) {
        _uiState.update { MyPageViewState.Default(nickname = newNickname) }
    }

    fun onTapAlarmStatusButtonOccurred(alarmStatus: AlarmStatus) {
        setAlarmStatus(alarmStatus)
    }

    fun onTapBackgroundMusicStatusButtonOccurred() {
        setBackgroundMusicStatus()
    }

    fun onTapBackgroundMusicChangeButtonOccurred() {
        setBackgroundMusicSelectState()
    }

    fun onBackgroundMusicChangedOccurred(musicName: String) {
        changeBackgroundMusic(musicName)
    }

    fun onTapTermsButtonOccurred() {
    }

    fun onTapChangePasswordButtonOccurred() {
    }

    fun onTapLogOutButtonOccurred() {
    }

    fun onInitOccurred() {
        viewModelScope.launch {
            when (val result = getUserInfoUseCase()) {
                is Result.Success -> {
                    _uiState.update {
                        MyPageViewState.Default(
                            id = result.data.userEmail,
                            nickname = result.data.userNickname,
                            backgroundMusicStatus = BackgroundMusicStatus.ON,
                            alarmStatus = AlarmStatus.ON,
                            backgroundMusic = "봄날",
                        )
                    }
                }

                is Result.Failure -> {}
            }
        }
    }

    private fun setBackgroundMusicSelectState() {
        _uiState.update {
            MyPageViewState.BackgroundMusicSelectState(
                it.id,
                it.nickname,
                it.backgroundMusicStatus,
                it.alarmStatus,
                it.backgroundMusic,
            )
        }
    }

    private fun setEditNicknameState() {
        _uiState.update {
            MyPageViewState.NicknameEditState(
                it.id,
                it.nickname,
                it.backgroundMusicStatus,
                it.alarmStatus,
                it.backgroundMusic,
            )
        }
    }

    private fun setDefaultState() {
        _uiState.update {
            MyPageViewState.Default(
                it.id,
                it.nickname,
                it.backgroundMusicStatus,
                it.alarmStatus,
                it.backgroundMusic,
            )
        }
    }

    private fun setAlarmStatus(alarmStatus: AlarmStatus) {
        when (val currentState = _uiState.value) {
            is MyPageViewState.Default ->
                _uiState.value =
                    currentState.copy(alarmStatus = alarmStatus)

            is MyPageViewState.NicknameEditState -> {}
            is MyPageViewState.BackgroundMusicSelectState -> {}
        }
    }

    private fun setBackgroundMusicStatus() {
        when (val currentState = _uiState.value) {
            is MyPageViewState.Default -> {
                val newStatus =
                    if (currentState.backgroundMusicStatus == BackgroundMusicStatus.ON) {
                        BackgroundMusicStatus.OFF
                    } else {
                        BackgroundMusicStatus.ON
                    }
                _uiState.value = currentState.copy(backgroundMusicStatus = newStatus)
            }

            is MyPageViewState.NicknameEditState -> {}
            is MyPageViewState.BackgroundMusicSelectState -> {}
        }
    }

    private fun changeBackgroundMusic(newBackgroundMusic: String) {
        _uiState.update {
            MyPageViewState.Default(
                it.id,
                it.nickname,
                it.backgroundMusicStatus,
                it.alarmStatus,
                newBackgroundMusic,
            )
        }
    }
}
