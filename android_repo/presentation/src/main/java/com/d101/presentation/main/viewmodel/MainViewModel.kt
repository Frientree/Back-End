package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import com.d101.domain.usecase.main.ChangeTreeNameUseCase
import com.d101.domain.usecase.main.DecideTodayFruitUseCase
import com.d101.domain.usecase.main.GetMessageFromTreeUseCase
import com.d101.domain.usecase.main.GetRandomLeafUseCase
import com.d101.domain.usecase.main.GetTodayFruitUseCase
import com.d101.domain.usecase.main.MakeFruitBySpeechUseCase
import com.d101.domain.usecase.main.MakeFruitByTextUseCase
import com.d101.domain.usecase.main.SendMyLeafUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val changeTreeNameUseCase: ChangeTreeNameUseCase,
    private val decideTodayFruitUseCase: DecideTodayFruitUseCase,
    private val getMessageFromTreeUseCase: GetMessageFromTreeUseCase,
    private val getRandomLeafUseCase: GetRandomLeafUseCase,
    private val getTodayFruitUseCase: GetTodayFruitUseCase,
    private val makeFruitBySpeechUseCase: MakeFruitBySpeechUseCase,
    private val makeFruitByTextUseCase: MakeFruitByTextUseCase,
    private val sendMyLeafUseCase: SendMyLeafUseCase,
) : ViewModel() {
}
