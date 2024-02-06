package com.d101.presentation.mypage

import android.os.Bundle
import android.text.TextWatcher
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
        setTextWatcher()
        collectUiState()
    }

    private fun setTextWatcher() {
        binding.currentPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                viewModel.setCurrentPassword(s.toString())
            }
        })
        binding.newPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                viewModel.setNewPassword(s.toString())
            }
        })
        binding.confirmPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                viewModel.setConfirmPassword(s.toString())
            }
        })
    }

    private fun setBinding() {
        binding.viewModel = viewModel
    }

    private fun collectUiState() {
        repeatOnStarted {
            viewModel.uiState.collect {
                binding.currentPasswordErrorTextView.setText(it.currentPasswordDescription)
                binding.newPasswordErrorTextView.setText(it.newPasswordDescription)
                binding.confirmPasswordErrorTextView.setText(it.passwordConfirmDescription)
            }
        }
    }
}
