package com.d101.presentation.main.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentMainBinding
import com.d101.presentation.main.fragments.dialogs.BeforeFruitCreateBaseFragment
import com.d101.presentation.main.fragments.dialogs.FruitDialogInterface
import com.d101.presentation.main.fragments.dialogs.TodayFruitFragment
import com.d101.presentation.main.state.TreeFragmentViewState
import com.d101.presentation.main.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import utils.repeatOnStarted

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MainFragmentViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: DialogFragment

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
                    dialog = BeforeFruitCreateBaseFragment()
                    FruitDialogInterface.dialog = dialog
                    dialog.show(childFragmentManager, "")
                }

                TreeFragmentViewState.FruitCreated -> {
                    viewModel.getTodayFruitFromDataModule()
                    dialog = TodayFruitFragment()
                    dialog.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.show(childFragmentManager, "")
                }

                else -> {
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.currentViewState.collect {
                when (it) {
                    is TreeFragmentViewState.FruitNotCreated -> {
                        binding.fruitStatusTextview.text = "오늘의 열매가 아직 열리지 않았어요!"
                        binding.createFruitButton.text = "오늘의 열매 만들기"
                    }

                    is TreeFragmentViewState.FruitCreated -> {
                        binding.fruitStatusTextview.text = "오늘의 열매를 확인해보세요!"
                        binding.createFruitButton.text = "오늘의 열매 확인하기"
                    }

                    is TreeFragmentViewState.TreeMessageChanged -> {
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getUserStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
