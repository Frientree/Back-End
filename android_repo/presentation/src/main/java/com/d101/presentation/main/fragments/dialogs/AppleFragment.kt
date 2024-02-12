package com.d101.presentation.main.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.d101.domain.model.FruitResources
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentAppleBinding
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import utils.CustomToast
import utils.darkenColor
import utils.repeatOnStarted
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class AppleFragment : Fragment() {

    private var _binding: FragmentAppleBinding? = null
    private val binding get() = _binding!!
    val timer = Timer()

    var flip = true

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

        setVisibility(true)
        val appleColorResource = FruitResources.APPLE
        appleColorResource.let { fruitResources ->
            val backgroundColor = resources.getColor(fruitResources.color, null)
            binding.fruitDescriptionCardView.setCardBackgroundColor(
                backgroundColor,
            )

            binding.fruitDescriptionCardView.strokeColor =
                backgroundColor.darkenColor()

            Glide.with(requireContext())
                .asGif()
                .load(fruitResources.fallingImage)
                .into(binding.fruitDetailBackgroundImageView)
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.appleUiState.collect {
                setVisibility(it)
            }
        }

        var repeatCnt = 0
        val timerTask = object : TimerTask() {
            override fun run() {
                viewModel.cardFlip(appleColorResource.color)
                repeatCnt++
                if (repeatCnt == 6) timer.cancel()
            }
        }
        timer.schedule(timerTask, 0, 200)
    }

    private fun setVisibility(flip: Boolean) {
        if (flip) {
            View.GONE.let {
                binding.fruitImageView.visibility = it
                binding.fruitNameTextView.visibility = it
                binding.fruitDescriptionTextView.visibility = it
                binding.fruitDescriptionCardView.visibility = it
                binding.fruitNameTextView.visibility = it
            }
        } else {
            View.VISIBLE.let {
                binding.fruitImageView.visibility = it
                binding.fruitNameTextView.visibility = it
                binding.fruitDescriptionTextView.visibility = it
                binding.fruitDescriptionCardView.visibility = it
                binding.fruitNameTextView.visibility = it
            }
        }
    }

    private fun showToast(message: String) =
        CustomToast.createAndShow(requireContext(), message)

    override fun onDestroyView() {
        super.onDestroyView()
        showToast("열매가 저장되었습니다!")
        timer.cancel()
        _binding = null
    }
}
