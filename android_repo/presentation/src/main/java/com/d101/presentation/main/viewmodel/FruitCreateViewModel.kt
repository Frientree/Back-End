package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.AppleData
import com.d101.domain.model.FruitCreated
import com.d101.domain.model.Result
import com.d101.domain.model.status.ErrorStatus
import com.d101.domain.model.status.FruitErrorStatus
import com.d101.domain.usecase.main.DecideTodayFruitUseCase
import com.d101.domain.usecase.main.MakeFruitBySpeechUseCase
import com.d101.domain.usecase.main.MakeFruitByTextUseCase
import com.d101.domain.usecase.usermanagement.ManageUserStatusUseCase
import com.d101.presentation.main.event.CreateFruitDialogViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.MutableEventFlow
import utils.asEventFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FruitCreateViewModel @Inject constructor(
    private val makeFruitByTextUseCase: MakeFruitByTextUseCase,
    private val makeFruitBySpeechUseCase: MakeFruitBySpeechUseCase,
    private val decideTodayFruitUseCase: DecideTodayFruitUseCase,
    private val manageUserStatusUseCase: ManageUserStatusUseCase,
) : ViewModel() {
    private val _todayFruitList: MutableStateFlow<List<FruitCreated>> = MutableStateFlow(listOf())
    val todayFruitList: StateFlow<List<FruitCreated>> = _todayFruitList.asStateFlow()

    private val _selectedFruit: MutableStateFlow<FruitCreated> = MutableStateFlow(FruitCreated())
    val selectedFruit: StateFlow<FruitCreated> = _selectedFruit.asStateFlow()

    val inputText = MutableStateFlow("")
    private lateinit var audioFile: File

    var isTextInput = true

    private val _eventFlow = MutableEventFlow<CreateFruitDialogViewEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private var _apple: AppleData? = null
    val apple get() = _apple!!

    fun setTodayFruitList() {
        viewModelScope.launch {
            val delay = async {
                delay(3_000L)
            }

            val fruitResult = if (isTextInput) {
                async {
                    makeFruitByTextUseCase(inputText.value)
                }
            } else {
                async {
                    makeFruitBySpeechUseCase(audioFile)
                }
            }
            delay.await()
            val result = fruitResult.await()
            when (result) {
                is Result.Success -> {
                    _todayFruitList.update { result.data }
                }

                is Result.Failure -> {
                    when (result.errorStatus) {
                        is FruitErrorStatus.LocalInsertError -> emitEvent(
                            CreateFruitDialogViewEvent.ShowErrorToastEvent(
                                "결과가 저장되지 못했습니다.",
                            ),
                        )

                        is ErrorStatus.NetworkError -> emitEvent(
                            CreateFruitDialogViewEvent.ShowErrorToastEvent(
                                "네트워크 에러입니다.",
                            ),
                        )

                        else -> {
                            emitEvent(
                                CreateFruitDialogViewEvent
                                    .ShowErrorToastEvent("예기치 못한 에러가 발생했습니다."),
                            )
                        }
                    }
                }
            }
        }
    }

    private val _appleUiState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val appleUiState: StateFlow<Boolean> = _appleUiState.asStateFlow()

    fun setAppleViewVisibility(flip: Boolean) {
        _appleUiState.update { flip }
    }

    fun cardFlip(fruitColorValue: Int) {
        emitEvent(CreateFruitDialogViewEvent.CardFlipEvent(fruitColorValue))
    }

    fun setAudioFile(file: File) {
        audioFile = file
    }

    fun setSelectedFruit(fruit: FruitCreated) {
        _selectedFruit.update { fruit }
    }

    fun saveSelectedFruit() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = decideTodayFruitUseCase(selectedFruit.value.fruitNum)) {
                is Result.Success -> {
                    manageUserStatusUseCase.updateUserStatus()
                    if (result.data.isApple) {
                        _apple = result.data
                        emitEvent(CreateFruitDialogViewEvent.AppleEvent(true))
                    } else {
                        emitEvent(CreateFruitDialogViewEvent.AppleEvent(false))
                    }
                }

                is Result.Failure -> {
                    when (result.errorStatus) {
                        is FruitErrorStatus.LocalInsertError -> emitEvent(
                            CreateFruitDialogViewEvent.ShowErrorToastEvent(
                                "결과가 저장되지 못했습니다.",
                            ),
                        )

                        is ErrorStatus.NetworkError -> emitEvent(
                            CreateFruitDialogViewEvent.ShowErrorToastEvent(
                                "네트워크 에러입니다.",
                            ),
                        )

                        else -> {
                            emitEvent(
                                CreateFruitDialogViewEvent
                                    .ShowErrorToastEvent("예기치 못한 에러가 발생했습니다."),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun emitEvent(event: CreateFruitDialogViewEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun onGoSelectInputTypeView() {
        emitEvent(CreateFruitDialogViewEvent.SelectInputTypeViewEvent)
    }

    fun onGoFruitLoadingView() {
        emitEvent(CreateFruitDialogViewEvent.FruitCreationLoadingViewEvent)
    }

    fun onGoReultView() {
        emitEvent(CreateFruitDialogViewEvent.AfterFruitCreationViewEvent)
    }

    fun onGoCreateionByTextView() {
        emitEvent(CreateFruitDialogViewEvent.FruitCreationByTextViewEvent)
    }

    fun onGoCreateionBySpeechView() {
        emitEvent(CreateFruitDialogViewEvent.FruitCreationBySpeechViewEvent)
    }
}
