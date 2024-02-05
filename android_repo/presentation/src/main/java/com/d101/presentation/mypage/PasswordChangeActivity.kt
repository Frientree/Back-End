package com.d101.presentation.mypage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.d101.presentation.databinding.ActivityPasswordChangeBinding
import com.d101.presentation.mypage.viewmodel.PasswordChangeViewModel
import dagger.hilt.android.AndroidEntryPoint
import utils.repeatOnStarted

@AndroidEntryPoint
class PasswordChangeActivity : AppCompatActivity() {

    private val binding: ActivityPasswordChangeBinding by lazy {
        ActivityPasswordChangeBinding.inflate(layoutInflater)
    }

    private val viewModel: PasswordChangeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setBinding()
        collectUiState()
    }

    private fun setBinding() {
        binding.viewModel = viewModel
    }

    private fun collectUiState() {
        repeatOnStarted {
            viewModel.uiState.collect {
            }
        }
    }
}
