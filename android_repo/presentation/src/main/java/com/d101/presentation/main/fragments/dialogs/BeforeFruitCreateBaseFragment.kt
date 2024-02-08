package com.d101.presentation.main.fragments.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentBeforeFruitCreateBaseBinding
import com.d101.presentation.main.event.CreateFruitDialogViewEvent
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import com.navercorp.nid.NaverIdLoginSDK.getApplicationContext
import dagger.hilt.android.AndroidEntryPoint
import utils.CustomToast
import utils.repeatOnStarted

@AndroidEntryPoint
class BeforeFruitCreateBaseFragment : DialogFragment() {
    private val viewModel: FruitCreateViewModel by viewModels()
    private var _binding: FragmentBeforeFruitCreateBaseBinding? = null
    private val binding get() = _binding!!

    private var flip = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBeforeFruitCreateBaseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewModel.onGoSelectInputTypeView()

        val scale: Float = getApplicationContext().getResources().getDisplayMetrics().density
        val distance: Float =
            binding.fruitDialogCardView.getCameraDistance() * (scale + (scale / 3))

        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect {
                when (it) {
                    is CreateFruitDialogViewEvent.SelectInputTypeViewEvent -> {
                        navigateToDestinationFragment(SelectInputTypeFragment())
                    }

                    is CreateFruitDialogViewEvent.FruitCreationBySpeechViewEvent -> {
                        navigateToDestinationFragment(FruitCreationBySpeechFragment())
                    }

                    is CreateFruitDialogViewEvent.FruitCreationByTextViewEvent -> {
                        navigateToDestinationFragment(FruitCreationByTextFragment())
                    }

                    is CreateFruitDialogViewEvent.FruitCreationLoadingViewEvent -> {
                        navigateToDestinationFragment(FruitCreationLoadingFragment())
                    }

                    is CreateFruitDialogViewEvent.AfterFruitCreationViewEvent -> {
                        navigateToDestinationFragment(AfterFruitCreateFragment())
                    }

                    is CreateFruitDialogViewEvent.AppleEvent -> {
                        if (it.isApple) {
                            navigateToDestinationFragment(AppleFragment())
                        } else {
                            showToast("열매가 저장되었습니다!")
                            dialog?.dismiss()
                        }
                    }

                    is CreateFruitDialogViewEvent.ShowErrorToastEvent -> {
                        showToast(it.message)
                        dialog?.dismiss()
                    }

                    is CreateFruitDialogViewEvent.CardFlipEvent -> {
                        flipAnimation(it.color, flip, distance)
                        flip = !flip
                    }
                }
            }
        }
    }
    private fun flipAnimation(fruitColorValue: Int, flip: Boolean, distance: Float) {
        binding.fruitDialogCardView.setCameraDistance(distance)
        binding.fruitDialogCardView.animate().withLayer()
            .rotationY(90F)
            .setDuration(150)
            .withEndAction {
                if (flip) {
                    binding.fruitDialogCardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            fruitColorValue,
                        ),
                    )
                    viewModel.setAppleViewVisibility(flip)
                } else {
                    binding.fruitDialogCardView.setCardBackgroundColor(Color.WHITE)
                    viewModel.setAppleViewVisibility(flip)
                }
                binding.fruitDialogCardView.setRotationY(-90F)
                binding.fruitDialogCardView.animate().withLayer()
                    .rotationY(0F)
                    .setDuration(250)
                    .start()
            }.start()
    }

    private fun showToast(message: String) =
        CustomToast.createAndShow(requireContext(), message)

    private fun navigateToDestinationFragment(destination: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.before_fragment_container_view, destination)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
