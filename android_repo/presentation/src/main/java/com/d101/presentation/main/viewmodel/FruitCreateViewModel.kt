package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.FruitCreated
import com.d101.domain.usecase.main.MakeFruitBySpeechUseCase
import com.d101.domain.usecase.main.MakeFruitByTextUseCase
import com.d101.presentation.main.state.CreateFruitDialogViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
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
) : ViewModel() {
    private val _todayFruitList: MutableStateFlow<List<FruitCreated>> = MutableStateFlow(listOf())
    val todayFruitList: StateFlow<List<FruitCreated>> = _todayFruitList.asStateFlow()

    val inputText = MutableStateFlow("")
    private lateinit var audioFile: File

    var isTextInput = true

    private val _selectedFruit: MutableStateFlow<FruitCreated> = MutableStateFlow(FruitCreated())
    val selectedFruit: StateFlow<FruitCreated> = _selectedFruit.asStateFlow()

    fun setAudioFile(file: File) {
        audioFile = file
    }

    fun setSelectedFruit(fruit: FruitCreated) {
        _selectedFruit.update { fruit }
    }

    fun setTodayFruitListByText() {
        viewModelScope.launch {
            delay(3_000L)
            val result = makeFruitByTextUseCase(inputText.value)
            // 성공 실패 로직 추가
            _todayFruitList.update { result }
        }
    }

    fun setTodayFruitListBySpeech() {
        viewModelScope.launch {
            delay(3_000L)
            val result = makeFruitBySpeechUseCase(audioFile)
            // 성공 실패 로직 추가
            _todayFruitList.update { result }
        }
    }

    private val _currentViewState: MutableStateFlow<CreateFruitDialogViewEvent> =
        MutableStateFlow(CreateFruitDialogViewEvent.SelectInputTypeViewEvent)
    val currentViewState: StateFlow<CreateFruitDialogViewEvent> = _currentViewState.asStateFlow()

    fun changeViewState(state: CreateFruitDialogViewEvent) {
        _currentViewState.update { state }
    }
}
