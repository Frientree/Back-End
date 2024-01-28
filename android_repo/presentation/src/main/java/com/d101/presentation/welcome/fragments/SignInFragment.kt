package com.d101.presentation.welcome.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentSignInBinding
import com.d101.presentation.main.MainActivity
import com.d101.presentation.welcome.event.SignInViewEvent
import com.d101.presentation.welcome.state.SignInViewState
import com.d101.presentation.welcome.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import utils.repeatOnStarted

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        collectEvent()
        collectUiState()
        // 임시 화면이동 로직 추후 재정의 후 제거
        binding.signInButton.setOnClickListener {
//            val intent = Intent(requireContext(), MainActivity::class.java)
//            startActivity(intent)
//            requireActivity().finish()
            viewModel.event(SignInViewEvent.OnSignInViewByFrientree)
        }

        binding.signUpTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_termsAgreeFragment)
        }

        binding.findPasswordTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_findPasswordFragment)
        }
    }

    private fun setBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun collectUiState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collect { state ->
                when (state) {
                    SignInViewState.Default -> {
                        Unit
                    }

                    is SignInViewState.SignInFailure -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }

                    SignInViewState.SignInSuccess -> {
                        viewModel.event(SignInViewEvent.OnNavigateToMain)
                    }
                }
            }
        }
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is SignInViewEvent.OnSignInViewByFrientree -> viewModel.signInByFrientree()
                    SignInViewEvent.OnNavigateToFindPassword -> {
                        findNavController().navigate(
                            R.id.action_signInFragment_to_findPasswordFragment,
                        )
                    }

                    SignInViewEvent.OnNavigateToTermsAgree -> {
                    }

                    SignInViewEvent.OnSignInViewByKakao -> {
                    }

                    SignInViewEvent.OnNavigateToMain -> {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
