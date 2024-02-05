package com.d101.presentation.welcome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.databinding.FragmentTermsAgreeBinding
import com.d101.presentation.welcome.adapter.TermsListAdapter
import com.d101.presentation.welcome.viewmodel.TermsAgreeViewModel

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
    }

    private fun setRecyclerView() {
        adapter = TermsListAdapter()
        binding.termsAgreeRecyclerView.adapter = adapter
    }

    private fun setBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
