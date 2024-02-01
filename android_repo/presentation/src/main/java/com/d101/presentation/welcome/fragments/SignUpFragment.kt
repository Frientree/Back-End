package com.d101.presentation.welcome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentSignUpBinding
import com.d101.presentation.welcome.event.SignUpEvent
import com.d101.presentation.welcome.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import utils.repeatOnStarted

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        collectEvent()
    }

    private fun setBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collect {
                binding.idInputLayout.setInputDataState(it.idInputState)
                binding.emailCheckInputLayout.setInputDataState(it.authCodeInputState)
                binding.nicknameInputLayout.setInputDataState(it.nickNameInputState)
                binding.passwordInputLayout.setInputDataState(it.passwordInputState)
                binding.passwordCheckInputLayout.setInputDataState(it.confirmPasswordInputState)
            }
        }
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { evnet ->
                when (evnet) {
                    SignUpEvent.EmailCheckAttempt -> viewModel.createAuthCode()
                    SignUpEvent.AuthCodeCheckAttempt -> viewModel.checkAuthCode()
                    SignUpEvent.SetDefault -> viewModel.setDefaultState()
                    SignUpEvent.SignUpAttempt -> Toast.makeText(
                        requireContext(),
                        "회원가입 시도",
                        Toast.LENGTH_SHORT,
                    ).show()

                    is SignUpEvent.SignUpFailure -> showToast(evnet.message)
                    is SignUpEvent.SignUpSuccess -> navigateToSignIn()
                }
            }
        }
    }

    private fun navigateToSignIn() {
        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
    }

    private fun showToast(message: String) = Toast.makeText(
        requireContext(),
        message,
        Toast.LENGTH_SHORT,
    ).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
