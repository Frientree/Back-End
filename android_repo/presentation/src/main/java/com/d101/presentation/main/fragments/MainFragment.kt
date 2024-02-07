package com.d101.presentation.main.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentMainBinding
import com.d101.presentation.main.fragments.dialogs.BeforeFruitCreateBaseFragment
import com.d101.presentation.main.fragments.dialogs.FruitDialogInterface
import com.d101.presentation.main.fragments.dialogs.TodayFruitFragment
import com.d101.presentation.main.state.TreeFragmentEvent
import com.d101.presentation.main.state.TreeMessageState
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

        viewModel.updateUserStatus()

        binding.createFruitButton.setOnClickListener {
            viewModel.onButtonClick()
        }

        binding.mainTreeImagebutton.setOnClickListener {
            viewModel.onGetTreeMessage()
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.messageState.collect {
                when (it) {
                    is TreeMessageState.EnableMessage -> {
                        binding.mainTreeImagebutton.isEnabled = true
                    }
                    is TreeMessageState.NoAccessMessage -> {
                        binding.mainTreeImagebutton.isEnabled = false
                    }
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect {
                when (it) {
                    is TreeFragmentEvent.MakeFruitEvent -> {
                        dialog = BeforeFruitCreateBaseFragment()
                        FruitDialogInterface.dialog = dialog
                        dialog.show(childFragmentManager, "")
                    }

                    is TreeFragmentEvent.CheckTodayFruitEvent -> {
                        dialog = TodayFruitFragment()
                        dialog.dialog?.window?.setBackgroundDrawable(
                            ColorDrawable(Color.TRANSPARENT),
                        )
                        dialog.show(childFragmentManager, "")
                    }

                    is TreeFragmentEvent.ShowErrorEvent -> {
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is TreeFragmentEvent.ChangeTreeMessage -> {
                        typingAnimation(it.message)
                    }
                }
            }
        }
    }
    private fun typingAnimation(message: String) {
        val sb = StringBuilder()
        var startDelay: Long = 200
        for (idx in 0 until message.length) {
            Handler().postDelayed({
                sb.append(message[idx])
                binding.treeSpeechTextview.text = sb
                if (idx == message.length - 1) viewModel.enableMessage()
            }, startDelay)
            startDelay += 50
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
