package com.d101.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _todayDate: MutableStateFlow<String> = MutableStateFlow("year / month / day")
    val todayDate: StateFlow<String> = _todayDate.asStateFlow()

    fun initTodayDate() {
        val localDate: LocalDate = LocalDate.now()

        _todayDate.update {
            String.format(
                "%d / %d / %d",
                localDate.year,
                localDate.monthValue,
                localDate.dayOfMonth,
            )
        }
    }
}
