package com.d101.presentation.main.fragments.dialogs

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentAfterFruitCreateBinding
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import com.d101.presentation.model.FruitResources
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import utils.repeatOnStarted

@AndroidEntryPoint
class AfterFruitCreateFragment : Fragment() {

    private var _binding: FragmentAfterFruitCreateBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FruitCreateViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_after_fruit_create,
            container,
            false,
        )
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val fruitList = viewModel.todayFruitList.value

        viewModel.setSelectedFruit(fruitList[0])

        viewLifecycleOwner.repeatOnStarted {
            viewModel.selectedFruit.collect {
                val fruitColorValue =
                    FruitResources.entries.find {
                        it.fruitEmotion == viewModel.selectedFruit.value.fruitFeel
                    }?.color ?: R.color.main_green

                binding.fruitDescriptionCardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        fruitColorValue,
                    ),
                )
            }
        }
        for (idx in 0..2) {
            val chip = binding.fruitsChipGroup.getChildAt(idx)
            if (chip is Chip) {
                Glide.with(binding.root.context).load(fruitList[idx].fruitImageUrl)
                    .apply(RequestOptions().override(Target.SIZE_ORIGINAL))
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(
                            chipImg: Drawable,
                            trainsition: Transition<in Drawable>?,
                        ) {
                            chip.chipIcon = chipImg
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
                chip.text = fruitList[idx].fruitFeel.korean
            }
        }

        var lastCheckedId = binding.fruitsChipGroup.checkedChipId
        binding.fruitsChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                binding.fruitsChipGroup.check(lastCheckedId)
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
            viewModel.saveSelectedFruit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
