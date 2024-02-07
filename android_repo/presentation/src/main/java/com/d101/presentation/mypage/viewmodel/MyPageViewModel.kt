package com.d101.presentation.mypage.viewmodel

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.usecase.mypage.ChangeBackgroundMusicUseCase
import com.d101.domain.usecase.mypage.ChangeUserNicknameUseCase
import com.d101.domain.usecase.mypage.LogOutUseCase
import com.d101.domain.usecase.mypage.SetAlarmStatusUseCase
import com.d101.domain.usecase.mypage.SetBackgroundMusicStatusUseCase
import com.d101.domain.usecase.usermanagement.GetUserInfoUseCase
import com.d101.presentation.mapper.UserMapper.toUserUiModel
import com.d101.presentation.mypage.event.MyPageViewEvent
import com.d101.presentation.mypage.state.AlarmStatus
import com.d101.presentation.mypage.state.BackgroundMusicStatus
import com.d101.presentation.mypage.state.MyPageViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val changeUserNicknameUseCase: ChangeUserNicknameUseCase,
    private val setAlarmStatusUseCase: SetAlarmStatusUseCase,
    private val setBackgroundMusicStatusUseCase: SetBackgroundMusicStatusUseCase,
    private val changeBackgroundMusicUseCase: ChangeBackgroundMusicUseCase,
    private val logOutUseCase: LogOutUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<MyPageViewState> =
        MutableStateFlow(MyPageViewState.Default())
    val uiState: StateFlow<MyPageViewState> = _uiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<MyPageViewEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    init {
        viewModelScope.launch { _eventFlow.emit(MyPageViewEvent.Init) }
    }

    fun onTapNicknameEditButtonOccurred() {
        setEditNicknameState()
    }

    fun onTapNicknameEditCancelButtonOccurred() {
        setDefaultState()
    }

    fun onChangeNicknameOccurred(nicknameInput: String) {
        viewModelScope.launch {
            when (val result = changeUserNicknameUseCase(nicknameInput)) {
                is Result.Success -> {
                    setDefaultState()
                }

                is Result.Failure -> when (result.errorStatus) {
                    is ErrorStatus.BadRequest -> {
                        _eventFlow.emit(MyPageViewEvent.OnShowToast("닉네임은 1글자 이상 8글자 이하로 입력해주세요"))
                    }

                    else -> {
                        _eventFlow.emit(MyPageViewEvent.OnShowToast("네트워크 에러"))
                    }
                }
            }
        }
    }

    fun onEditorAction(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onTapNicknameConfirmButton()
            return true
        }
        return false
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

    fun onTapSignOutButtonOccurred() {
        // TODO 회원탈퇴 로직
    }

    fun onTapLogOutButtonOccurred() {
        viewModelScope.launch(Dispatchers.IO) {
            when (logOutUseCase()) {
                is Result.Success -> _eventFlow.emit(MyPageViewEvent.OnLogOut)
                is Result.Failure -> _eventFlow.emit(MyPageViewEvent.OnShowToast("로그아웃 실패"))
            }
        }
    }

    fun onInitOccurred() {
        viewModelScope.launch {
            getUserInfoUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        val uiModel = result.data.toUserUiModel()
                        _uiState.update {
                            MyPageViewState.Default(
                                isSocial = uiModel.isSocial,
                                id = uiModel.userEmail,
                                nickname = uiModel.userNickname,
                                backgroundMusicStatus = uiModel.backgroundMusicStatus,
                                alarmStatus = uiModel.alarmStatus,
                                backgroundMusic = uiModel.backgroundMusicName,
                            )
                        }
                    }

                    is Result.Failure -> {}
                }
            }
        }
    }

    private fun setBackgroundMusicSelectState() {
        _uiState.update {
            MyPageViewState.BackgroundMusicSelectState(
                it.isSocial,
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
                it.isSocial,
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
                it.isSocial,
                it.id,
                it.nickname,
                it.backgroundMusicStatus,
                it.alarmStatus,
                it.backgroundMusic,
            )
        }
    }

    private fun setAlarmStatus(alarmStatus: AlarmStatus) {
        viewModelScope.launch {
            when (val result = setAlarmStatusUseCase(alarmStatus == AlarmStatus.ON)) {
                is Result.Success -> {
                    setDefaultState()
                }

                is Result.Failure -> {
                    when (result.errorStatus) {
                        is ErrorStatus.BadRequest -> {
                            emitEvent(MyPageViewEvent.OnShowToast("알람 설정 실패"))
                        }

                        else -> {
                            emitEvent(MyPageViewEvent.OnShowToast("네트워크 에러"))
                        }
                    }
                }
            }
        }
    }

    private fun setBackgroundMusicStatus() {
        viewModelScope.launch {
            when (
                setBackgroundMusicStatusUseCase(
                    uiState.value.backgroundMusicStatus != BackgroundMusicStatus.ON,
                )
            ) {
                is Result.Success -> setDefaultState()
                is Result.Failure -> {
                    emitEvent(MyPageViewEvent.OnShowToast("배경음악 설정 실패"))
                }
            }
        }
    }

    private fun changeBackgroundMusic(newBackgroundMusic: String) {
        viewModelScope.launch {
            when (changeBackgroundMusicUseCase(newBackgroundMusic)) {
                is Result.Success -> setDefaultState()
                is Result.Failure -> {
                    emitEvent(MyPageViewEvent.OnShowToast("배경음악 변경 실패"))
                }
            }
        }
    }

    fun onTapNicknameEditButton() = emitEvent(MyPageViewEvent.OnTapNicknameEditButton)

    fun onTapNicknameEditCancelButton() = emitEvent(MyPageViewEvent.OnTapNicknameEditCancelButton)

    fun onTapNicknameConfirmButton() = emitEvent(MyPageViewEvent.OnTapNicknameConfirmButton)

    fun onTapAlarmStatusButton(alarmStatus: AlarmStatus) =
        emitEvent(MyPageViewEvent.OnTapAlarmStatusButton(alarmStatus))

    fun onTapBackgroundMusicStatusButton() =
        emitEvent(MyPageViewEvent.OnTapBackgroundMusicStatusButton)

    fun onTapBackgroundMusicChangeButton() =
        emitEvent(MyPageViewEvent.OnTapBackgroundMusicChangeButton)

    fun onBackgroundMusicChanged(musicName: String) =
        emitEvent(MyPageViewEvent.OnBackgroundMusicChanged(musicName))

    fun onTapTermsButton() = emitEvent(MyPageViewEvent.OnTapTermsButton)

    fun onTapChangePasswordButton() = emitEvent(MyPageViewEvent.OnTapChangePasswordButton)

    fun onTapLogOutButton() = emitEvent(MyPageViewEvent.OnTapLogOutButton)
    fun onTapSignOutButton() = emitEvent(MyPageViewEvent.OnTapSignOutButton)

    private fun emitEvent(event: MyPageViewEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }
}
