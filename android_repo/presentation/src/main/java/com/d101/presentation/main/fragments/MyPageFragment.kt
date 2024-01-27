package com.d101.presentation.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentMypageBinding
import com.d101.presentation.mypage.event.MyPageViewEvent
import com.d101.presentation.mypage.viewmodel.MyPageViewModel
import kotlinx.coroutines.launch
import utils.repeatOnStarted

class MyPageFragment : Fragment() {
    private val viewModel: MyPageViewModel by viewModels()
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBinding()

        subscribeEvent()

        viewModel.onEventOccurred(MyPageViewEvent.Init)
    }

    private fun subscribeEvent() {
        lifecycleScope.launch {
            repeatOnStarted {
                viewModel.event.collect {
                    viewModel.onEventOccurred(it)
                }
            }
        }
    }

    private fun setBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.myPageViewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
