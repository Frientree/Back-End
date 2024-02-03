package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.AppleData
import com.d101.domain.model.FruitCreated
import com.d101.domain.usecase.main.DecideTodayFruitUseCase
import com.d101.domain.usecase.main.MakeFruitBySpeechUseCase
import com.d101.domain.usecase.main.MakeFruitByTextUseCase
import com.d101.domain.usecase.usermanagement.UpdateUserStatusUseCase
import com.d101.presentation.main.state.CreateFruitDialogViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FruitCreateViewModel @Inject constructor(
    private val makeFruitByTextUseCase: MakeFruitByTextUseCase,
    private val makeFruitBySpeechUseCase: MakeFruitBySpeechUseCase,
    private val decideTodayFruitUseCase: DecideTodayFruitUseCase,
    private val updateUserStatusUseCase: UpdateUserStatusUseCase,
) : ViewModel() {
    private val _todayFruitList: MutableStateFlow<List<FruitCreated>> = MutableStateFlow(listOf())
    val todayFruitList: StateFlow<List<FruitCreated>> = _todayFruitList.asStateFlow()

    private val _selectedFruit: MutableStateFlow<FruitCreated> = MutableStateFlow(FruitCreated())
    val selectedFruit: StateFlow<FruitCreated> = _selectedFruit.asStateFlow()

    val inputText = MutableStateFlow("")
    private lateinit var audioFile: File

    var isTextInput = true

    // Event Flow를 사용하고 싶었으나...
//    private val _appleEvent: MutableStateFlow<AppleEvent> = MutableStateFlow(AppleEvent.isNotAppleEvent)
//    val appleEvent: StateFlow<AppleEvent> = _appleEvent.asStateFlow()

    private var _apple: AppleData? = null
    val apple get() = _apple!!

    fun setTodayFruitList() {
        viewModelScope.launch {
            delay(3_000L)
            val result = if (isTextInput) {
                makeFruitByTextUseCase(inputText.value)
                // 성공 실패 로직 추가
            } else {
                makeFruitBySpeechUseCase(audioFile)
                // 성공 실패 로직 추가
            }

            _todayFruitList.update { result }
        }
    }

    fun setAudioFile(file: File) {
        audioFile = file
    }

    fun setSelectedFruit(fruit: FruitCreated) {
        _selectedFruit.update { fruit }
    }

    fun saveSelectedFruit() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = decideTodayFruitUseCase(selectedFruit.value.fruitNum)
            updateUserStatusUseCase()
            if (result.isApple) {
                _apple = result
                changeViewEvent(CreateFruitDialogViewEvent.AppleEvent(true))
                // event(AppleEvent.isAppleEvent)
            } else {
                changeViewEvent(CreateFruitDialogViewEvent.AppleEvent(false))
                // event(AppleEvent.isNotAppleEvent)
            }
        }
    }
    // 실패 ...
//    private fun event(event: AppleEvent) {
//        viewModelScope.launch {
//            _appleEvent.emit(event)
//        }
//    }

    private val _currentViewState: MutableStateFlow<CreateFruitDialogViewEvent> =
        MutableStateFlow(CreateFruitDialogViewEvent.SelectInputTypeViewEvent)
    val currentViewState: StateFlow<CreateFruitDialogViewEvent> = _currentViewState.asStateFlow()

    fun changeViewEvent(state: CreateFruitDialogViewEvent) {
        _currentViewState.update { state }
    }
}
