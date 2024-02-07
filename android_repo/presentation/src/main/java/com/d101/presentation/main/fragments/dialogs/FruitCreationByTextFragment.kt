package com.d101.presentation.main.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentFruitCreationByTextBinding
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import utils.CustomToast

@AndroidEntryPoint
class FruitCreationByTextFragment : Fragment() {
    private val viewModel: FruitCreateViewModel by viewModels(
        { requireParentFragment() },
    )
    private var _binding: FragmentFruitCreationByTextBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_fruit_creation_by_text,
            container,
            false,
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.isTextInput = true

        binding.createFruitByTextButton
            .setOnClickListener {
                if (viewModel.inputText.value.isNotEmpty()) {
                    viewModel.onGoFruitLoadingView()
                } else {
                    showToast("텍스트를 채워주세요!")
                }
            }
    }

    private fun showToast(message: String) = CustomToast.createAndShow(requireContext(), message)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
