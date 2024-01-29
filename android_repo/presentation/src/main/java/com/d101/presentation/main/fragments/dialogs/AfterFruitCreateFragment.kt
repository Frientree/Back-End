package com.d101.presentation.main.fragments.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentAfterFruitCreateBinding
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class AfterFruitCreateFragment : Fragment() {

    private var _binding: FragmentAfterFruitCreateBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FruitCreateViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
//        _binding = FragmentAfterFruitCreateBinding.inflate(inflater)
//        return binding.root
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_after_fruit_create,
            container,
            false,
        )
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val fruitList = viewModel.todayFruitList.value

        viewModel.setSelectedFruit(fruitList[0])

        val fruitChipGroup = view.findViewById<ChipGroup>(R.id.fruits_chip_group)

        for (idx in 0..2) {
            val chip = fruitChipGroup.getChildAt(idx)
            if (chip is Chip) {
                // TODO :: 칩 아이콘으로 이미지 글라이드로 주입 필요
                chip.text = fruitList[idx].fruitFeel
            }
        }

        var lastCheckedId = fruitChipGroup.checkedChipId
        fruitChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                fruitChipGroup.check(lastCheckedId)
            } else {
                when (checkedIds[0]) {
                    R.id.fisrt_fruit_chip -> {
                        viewModel.setSelectedFruit(fruitList[0])
                        lastCheckedId = checkedIds[0]
                    }

                    R.id.second_fruit_chip -> {
                        viewModel.setSelectedFruit(fruitList[1])
                        lastCheckedId = checkedIds[0]
                    }

                    R.id.third_fruit_chip -> {
                        viewModel.setSelectedFruit(fruitList[2])
                        lastCheckedId = checkedIds[0]
                    }
                }
            }
        }

        binding.saveFruitButton.setOnClickListener {
            FruitDialogInterface.dialog.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
