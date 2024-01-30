package com.d101.presentation.main.fragments.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentBeforeFruitCreateBaseBinding
import com.d101.presentation.main.state.CreateFruitDialogViewEvent
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BeforeFruitCreateBaseFragment : DialogFragment() {
    private val viewModel: FruitCreateViewModel by viewModels()
    private var _binding: FragmentBeforeFruitCreateBaseBinding? = null
    private val binding get() = _binding!!

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
        viewModel.changeViewState(CreateFruitDialogViewEvent.SelectInputTypeViewEvent)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentViewState.collect {
                    when (it) {
                        CreateFruitDialogViewEvent.SelectInputTypeViewEvent -> {
                            navigateToDestinationFragment(SelectInputTypeFragment())
                        }

                        CreateFruitDialogViewEvent.FruitCreationBySpeechViewEvent -> {
                            navigateToDestinationFragment(FruitCreationBySpeechFragment())
                        }

                        CreateFruitDialogViewEvent.FruitCreationByTextViewEvent -> {
                            navigateToDestinationFragment(FruitCreationByTextFragment())
                        }

                        CreateFruitDialogViewEvent.FruitCreationLoadingViewEvent -> {
                            if (viewModel.isTextInput) {
                                navigateToDestinationFragment(
                                    FruitCreationLoadingFragment.newInstance(
                                        TEXT,
                                    ),
                                )
                            } else {
                                navigateToDestinationFragment(
                                    FruitCreationLoadingFragment.newInstance(
                                        SPEECH,
                                    ),
                                )
                            }
                        }

                        CreateFruitDialogViewEvent.AfterFruitCreationViewEvent -> {
                            navigateToDestinationFragment(AfterFruitCreateFragment())
                        }
                    }
                }
            }
        }
    }

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

    companion object {
        const val SPEECH = "speech"
        const val TEXT = "text"
    }
}
