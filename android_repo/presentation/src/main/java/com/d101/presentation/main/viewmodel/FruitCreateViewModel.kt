package com.d101.presentation.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d101.domain.model.FruitCreated
import com.d101.domain.usecase.main.MakeFruitByTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FruitCreateViewModel @Inject constructor(
    private val makeFruitByTextUseCase: MakeFruitByTextUseCase,
) : ViewModel() {
    private val _todayFruitList: MutableStateFlow<List<FruitCreated>> = MutableStateFlow(listOf())
    val todayFruitList: StateFlow<List<FruitCreated>> = _todayFruitList.asStateFlow()

    private lateinit var inputText: String

    private val _selectedFruit: MutableStateFlow<FruitCreated> = MutableStateFlow(FruitCreated())
    val selectedFruit: StateFlow<FruitCreated> = _selectedFruit.asStateFlow()

    fun setText(text: String) {
        inputText = text
    }

    fun setSelectedFruit(fruit: FruitCreated) {
        _selectedFruit.update { fruit }
    }

    fun setTodayFruitListByText() {
        Log.d("DEBUG:::", inputText)
        viewModelScope.launch {
            delay(4_000L)
            val result = makeFruitByTextUseCase(inputText)
            // 성공 실패 로직 추가
            _todayFruitList.update { result }
        }
    }
}
