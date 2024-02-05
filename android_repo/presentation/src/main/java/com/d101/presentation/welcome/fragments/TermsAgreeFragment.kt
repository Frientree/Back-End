package com.d101.presentation.welcome.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentTermsAgreeBinding
import com.d101.presentation.welcome.adapter.TermsListAdapter
import com.d101.presentation.welcome.event.TermsAgreeEvent
import com.d101.presentation.welcome.state.TermsAgreeState
import com.d101.presentation.welcome.viewmodel.TermsAgreeViewModel
import dagger.hilt.android.AndroidEntryPoint
import utils.repeatOnStarted

@AndroidEntryPoint
class TermsAgreeFragment : Fragment() {

    private var _binding: FragmentTermsAgreeBinding? = null
    val binding get() = _binding!!

    private val viewModel: TermsAgreeViewModel by viewModels()
    private lateinit var adapter: TermsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTermsAgreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        setRecyclerView()
        collectUiState()
        collectEvent()
    }

    private fun setRecyclerView() {
        adapter = TermsListAdapter(
            checkTerms = { viewModel.onCheckedTerms(it) },
            showTermsInfo = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            },
        )
        binding.termsAgreeRecyclerView.adapter = adapter
    }

    private fun setBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun collectUiState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.termsList)
                when (state) {
                    is TermsAgreeState.TermsAllAgreedState -> {
                    }

                    is TermsAgreeState.TermsDisagreeAbsentState -> {
                    }

                    is TermsAgreeState.TermsLoadingState -> {}
                }
            }
        }
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is TermsAgreeEvent.Init -> viewModel.onInitOccurred()

                    is TermsAgreeEvent.OnCheckAgree -> viewModel.checkTerms(event.termsItem)

                    is TermsAgreeEvent.OnCheckAllAgree -> viewModel.checkAllTerms()

                    is TermsAgreeEvent.OnClickConfirmButton -> navigateToSignUp()

                    is TermsAgreeEvent.OnShowToast -> showToast(event.message)
                }
            }
        }
    }

    private fun navigateToSignUp() =
        findNavController().navigate(R.id.action_termsAgreeFragment_to_signUpFragment)

    private fun showToast(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
