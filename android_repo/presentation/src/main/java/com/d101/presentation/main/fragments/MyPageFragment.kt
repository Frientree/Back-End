package com.d101.presentation.main.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentMypageBinding
import com.d101.presentation.mypage.event.MyPageViewEvent
import com.d101.presentation.mypage.state.AlarmStatus
import com.d101.presentation.mypage.state.BackgroundMusicStatus
import com.d101.presentation.mypage.state.MyPageViewState
import com.d101.presentation.mypage.viewmodel.MyPageViewModel
import kotlinx.coroutines.launch
import utils.repeatOnStarted

class MyPageFragment : Fragment() {
    private val viewModel: MyPageViewModel by viewModels()
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBinding()
        subscribeEvent()
        subScribeViewState()
        viewModel.onEventOccurred(MyPageViewEvent.Init)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.pencilButtonImageView.setOnClickListener {
            viewModel.onEventOccurred(MyPageViewEvent.onTapNicknameEditButton)
        }

        binding.cancelButtonImageView.setOnClickListener {
            viewModel.onEventOccurred(MyPageViewEvent.onCancelNicknameEdit)
        }

        binding.alarmOnButtonTextView.setOnClickListener {
            viewModel.onEventOccurred(MyPageViewEvent.onSetAlarmStatus(AlarmStatus.ON))
        }

        binding.alarmOffButtonTextView.setOnClickListener {
            viewModel.onEventOccurred(MyPageViewEvent.onSetAlarmStatus(AlarmStatus.OFF))
        }

        binding.backgroundMusicOnOffButtonImageView.setOnClickListener {
            viewModel.onEventOccurred(MyPageViewEvent.onSetBackgroundMusicStatus)
        }
    }

    private fun setBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.myPageViewModel = viewModel
    }

    private fun subscribeEvent() {
        lifecycleScope.launch {
            repeatOnStarted {
                viewModel.event.collect {
                    viewModel.onEventOccurred(it)
                }
            }
        }
    }

    private fun subScribeViewState() {
        lifecycleScope.launch {
            repeatOnStarted {
                val inputMethodManager = requireContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE,
                ) as InputMethodManager
                viewModel.myPageViewState.collect { state ->
                    when (state) {
                        is MyPageViewState.Default -> {
                            setBackgroundMusicStatusUI(state)
                            setAlarmStatusUI(state)
                            binding.musicTextView.text = state.backgroundMusic
                            setDefaultUI(inputMethodManager)
                        }

                        is MyPageViewState.NicknameEditState -> {
                            setBackgroundMusicStatusUI(state)
                            setAlarmStatusUI(state)
                            binding.musicTextView.text = state.backgroundMusic
                            setNicknameEditUI(inputMethodManager)
                        }
                    }
                }
            }
        }
    }

    private fun setNicknameEditUI(inputMethodManager: InputMethodManager) {
        binding.pencilButtonImageView.visibility = View.GONE
        binding.nicknameConfirmButtonTextView.visibility = View.VISIBLE
        binding.cancelButtonImageView.visibility = View.VISIBLE
        binding.nicknameEditText.isEnabled = true
        binding.nicknameEditText.requestFocus()
        inputMethodManager.showSoftInput(binding.nicknameEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setDefaultUI(inputMethodManager: InputMethodManager) {
        binding.pencilButtonImageView.visibility = View.VISIBLE
        binding.nicknameConfirmButtonTextView.visibility = View.GONE
        binding.cancelButtonImageView.visibility = View.GONE
        binding.nicknameEditText.isEnabled = false
        inputMethodManager.hideSoftInputFromWindow(binding.nicknameEditText.windowToken, 0)
    }

    private fun setBackgroundMusicStatusUI(state: MyPageViewState) {
        when (state.backgroundMusicStatus) {
            BackgroundMusicStatus.ON ->
                binding.backgroundMusicOnOffButtonImageView.setImageResource(R.drawable.sound_on)

            BackgroundMusicStatus.OFF ->
                binding.backgroundMusicOnOffButtonImageView.setImageResource(R.drawable.sound_off)
        }
    }

    private fun setAlarmStatusUI(state: MyPageViewState) {
        when (state.alarmStatus) {
            AlarmStatus.ON -> {
                binding.alarmOnButtonTextView.setBackgroundResource(
                    R.drawable.btn_small_confirm_green,
                )
                binding.alarmOffButtonTextView.setBackgroundResource(R.drawable.btn_small_confirm)
            }

            AlarmStatus.OFF -> {
                binding.alarmOnButtonTextView.setBackgroundResource(R.drawable.btn_small_confirm)
                binding.alarmOffButtonTextView.setBackgroundResource(
                    R.drawable.btn_small_confirm_green,
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
