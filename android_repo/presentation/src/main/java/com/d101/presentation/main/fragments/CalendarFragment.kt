package com.d101.presentation.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.calendar.viewmodel.CalendarViewModel
import com.d101.presentation.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {
    private val viewModel: CalendarViewModel by viewModels()
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
