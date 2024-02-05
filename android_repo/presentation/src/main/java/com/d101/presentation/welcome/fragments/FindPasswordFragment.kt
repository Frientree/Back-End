package com.d101.presentation.welcome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d101.presentation.databinding.FragmentFindPasswordBinding
import com.d101.presentation.welcome.event.FindPasswordEvent
import com.d101.presentation.welcome.viewmodel.FindPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import utils.repeatOnStarted

@AndroidEntryPoint
class FindPasswordFragment : Fragment() {

    private var _binding: FragmentFindPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FindPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFindPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        collectUiState()
        collectEvent()
    }

    private fun setBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun collectUiState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collect {
                binding.emailInputLayout.setInputDataState(it)
            }
        }
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    FindPasswordEvent.Init -> viewModel.collectEmailChange()

                    FindPasswordEvent.OnReceivedTemporaryPassword -> {
                        showToast("임시 비밀번호가 발급되었습니다.")
                        navigateToSignIn()
                    }

                    FindPasswordEvent.OnFindPasswordAttempt -> viewModel.attemptFindPassword()
                    is FindPasswordEvent.OnShowToast -> showToast(event.message)
                }
            }
        }
    }

    private fun navigateToSignIn() = findNavController().navigateUp()

    private fun showToast(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
