package com.d101.presentation.main.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentMainBinding
import com.d101.presentation.main.fragments.dialogs.BeforeFruitCreateBaseFragment
import com.d101.presentation.main.fragments.dialogs.FruitDialogInterface
import com.d101.presentation.main.fragments.dialogs.TodayFruitFragment
import com.d101.presentation.main.state.TreeFragmentViewState
import com.d101.presentation.main.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MainFragmentViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = viewModel

        viewModel.initTodayDate()

        binding.createFruitButton.setOnClickListener {
            when (viewModel.currentViewState.value) {
                TreeFragmentViewState.FruitNotCreated -> {
                    val dialog = BeforeFruitCreateBaseFragment()
                    FruitDialogInterface.dialog = dialog
                    dialog.show(childFragmentManager, "")
                }
                TreeFragmentViewState.FruitCreated -> {
                    viewModel.getTodayFruitFromDataModule()
                    val dialog = TodayFruitFragment()
                    dialog.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.show(childFragmentManager, "")
                }
                else -> {}
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentViewState.collect {
                    when (it) {
                        is TreeFragmentViewState.FruitNotCreated -> {
                            binding.createFruitButton.text = "오늘의 열매 만들기"
                        }
                        is TreeFragmentViewState.FruitCreated -> {
                            binding.createFruitButton.text = "오늘의 열매 확인하기"
                        }

                        is TreeFragmentViewState.TreeMessageChanged -> {
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 1. dataStore에서 userFruitState를 가져온다.
        val userFruitStatus: Boolean = false
        // 2. change View State
        if (userFruitStatus) {
            viewModel.changeViewState(TreeFragmentViewState.FruitCreated)
        } else {
            viewModel.changeViewState(TreeFragmentViewState.FruitNotCreated)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
