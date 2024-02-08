package com.d101.presentation.main.fragments.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.d101.domain.model.FruitResources
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentTodayFruitBinding
import com.d101.presentation.main.viewmodel.MainFragmentViewModel

class TodayFruitFragment : DialogFragment() {

    private val viewModel: MainFragmentViewModel by viewModels({ requireParentFragment() })

    private var _binding: FragmentTodayFruitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_today_fruit, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        FruitResources.entries.find { it.fruitEmotion == viewModel.todayFruit.fruitEmotion }?.let {
            binding.fruitDescriptionCardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    it.color,
                ),
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
