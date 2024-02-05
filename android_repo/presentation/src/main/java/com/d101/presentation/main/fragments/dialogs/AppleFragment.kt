package com.d101.presentation.main.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentAppleBinding
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import com.d101.presentation.model.FruitResources
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppleFragment : Fragment() {

    private var _binding: FragmentAppleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FruitCreateViewModel by viewModels({ requireParentFragment() })
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_apple, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        FruitDialogInterface.dialog.isCancelable = true

        val fruitColorValue =
            FruitResources.entries.find {
                it.fruitEmotion == viewModel.selectedFruit.value.fruitFeel
            }?.color
                ?: R.color.main_green

        binding.fruitDescriptionCardView.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                fruitColorValue,
            ),
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
