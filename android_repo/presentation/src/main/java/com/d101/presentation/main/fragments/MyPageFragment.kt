package com.d101.presentation.main.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.BackgroundMusicPlayer
import com.d101.presentation.R
import com.d101.presentation.databinding.DialogBackgroundMusicSelectBinding
import com.d101.presentation.databinding.FragmentMypageBinding
import com.d101.presentation.mypage.event.MyPageViewEvent
import com.d101.presentation.mypage.state.AlarmStatus
import com.d101.presentation.mypage.state.BackgroundMusicStatus
import com.d101.presentation.mypage.state.MyPageViewState
import com.d101.presentation.mypage.viewmodel.MyPageViewModel
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
        subscribeViewState()
        viewModel.eventOccurred(MyPageViewEvent.Init)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.pencilButtonImageView.setOnClickListener {
            viewModel.eventOccurred(MyPageViewEvent.onTapNicknameEditButton)
        }

        binding.cancelButtonImageView.setOnClickListener {
            viewModel.eventOccurred(MyPageViewEvent.onCancelNicknameEdit)
        }

        binding.alarmOnButtonTextView.setOnClickListener {
            viewModel.eventOccurred(MyPageViewEvent.onSetAlarmStatus(AlarmStatus.ON))
        }

        binding.alarmOffButtonTextView.setOnClickListener {
            viewModel.eventOccurred(MyPageViewEvent.onSetAlarmStatus(AlarmStatus.OFF))
        }

        binding.backgroundMusicOnOffButtonImageView.setOnClickListener {
            viewModel.eventOccurred(MyPageViewEvent.onSetBackgroundMusicStatus)
        }

        binding.backgroundMusicChangeButtonTextView.setOnClickListener {
            viewModel.eventOccurred(MyPageViewEvent.onTapBackgroundMusicChangeButton)
        }
    }

    private fun setBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.myPageViewModel = viewModel
    }

    private fun subscribeEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect {
                viewModel.onEventOccurred(it)
            }
        }
    }

    private fun subscribeViewState() {
        viewLifecycleOwner.repeatOnStarted {
            val inputMethodManager = requireContext().getSystemService(
                Context.INPUT_METHOD_SERVICE,
            ) as InputMethodManager
            viewModel.myPageViewState.collect { state ->
                updateUI(state, inputMethodManager)
            }
        }
    }

    private fun updateUI(
        state: MyPageViewState,
        inputMethodManager: InputMethodManager
    ) {
        when (state) {
            is MyPageViewState.Default -> {
                setBackgroundMusicStatusUI(state)
                setAlarmStatusUI(state)
                binding.musicTextView.text = state.backgroundMusic
                setDefaultUI(inputMethodManager)
                //                            TODO: Marquee 효과 적용이 안되는 중..
                binding.musicTextView.requestFocus()
                binding.musicTextView.isSelected = true
            }

            is MyPageViewState.NicknameEditState -> {
                setBackgroundMusicStatusUI(state)
                setAlarmStatusUI(state)
                binding.musicTextView.text = state.backgroundMusic
                setNicknameEditUI(inputMethodManager)
                binding.musicTextView.requestFocus()
                binding.musicTextView.isSelected = true
            }

            is MyPageViewState.BackgroundMusicSelectState -> {
                showBackgroundMusicSelectDialog()
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
            BackgroundMusicStatus.ON -> {
                binding.backgroundMusicOnOffButtonImageView.setImageResource(R.drawable.sound_on)
                BackgroundMusicPlayer.resumeMusic()
            }

            BackgroundMusicStatus.OFF -> {
                binding.backgroundMusicOnOffButtonImageView.setImageResource(R.drawable.sound_off)
                BackgroundMusicPlayer.stopMusic()
            }
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

    private fun showBackgroundMusicSelectDialog() {
        val dialog = createFullScreenDialog()
        val dialogBinding = DialogBackgroundMusicSelectBinding.inflate(layoutInflater)
        val musicList = BackgroundMusicPlayer.getMusicList()

        dialogBinding.setMusicSelector(musicList, viewModel.myPageViewState.value.backgroundMusic)
        dialog.setDialogDismissListener(dialogBinding, musicList[dialogBinding.musicSelector.value])
        BackgroundMusicPlayer.resumeMusic()
        dialog.show()
    }

    private fun createFullScreenDialog(): Dialog {
        return Dialog(requireContext(), R.style.FullScreenDialogStyle).apply {
            window?.setBackgroundDrawableResource(R.drawable.btn_white_green_36dp)
        }
    }

    private fun Dialog.setDialogDismissListener(
        dialogBinding: DialogBackgroundMusicSelectBinding,
        musicName: String,
    ) {
        this.setContentView(dialogBinding.root)
        setOnDismissListener {
            viewModel.onEventOccurred(MyPageViewEvent.onChangeBackgroundMusic(musicName))
        }
    }

    private fun DialogBackgroundMusicSelectBinding.setMusicSelector(
        musicList: List<String>,
        currentMusic: String,
    ) {
        this.musicSelector.apply {
            minValue = 0
            maxValue = musicList.size - 1
            displayedValues = musicList.toTypedArray()
            value = musicList.indexOf(currentMusic)
            setOnValueChangedListener { _, _, selectedNow ->
                BackgroundMusicPlayer.playMusic(requireContext(), musicList[selectedNow])
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
