package com.d101.presentation.main.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.BackgroundMusicPlayer
import com.d101.presentation.R
import com.d101.presentation.databinding.DialogBackgroundMusicSelectBinding
import com.d101.presentation.databinding.FragmentMypageBinding
import com.d101.presentation.mypage.PasswordChangeActivity
import com.d101.presentation.mypage.event.MyPageViewEvent
import com.d101.presentation.mypage.state.AlarmStatus
import com.d101.presentation.mypage.state.BackgroundMusicStatus
import com.d101.presentation.mypage.state.MyPageViewState
import com.d101.presentation.mypage.viewmodel.MyPageViewModel
import com.d101.presentation.welcome.WelcomeActivity
import dagger.hilt.android.AndroidEntryPoint
import utils.repeatOnStarted

@AndroidEntryPoint
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
    }

    private fun setBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.myPageViewModel = viewModel
    }

    private fun subscribeEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is MyPageViewEvent.Init -> {
                        viewModel.onInitOccurred()
                    }

                    is MyPageViewEvent.OnTapNicknameEditButton -> {
                        viewModel.onTapNicknameEditButtonOccurred()
                    }

                    is MyPageViewEvent.OnTapNicknameEditCancelButton ->
                        viewModel.onTapNicknameEditCancelButtonOccurred()

                    is MyPageViewEvent.OnTapNicknameConfirmButton ->
                        viewModel.onChangeNicknameOccurred(binding.nicknameEditText.text.toString())

                    is MyPageViewEvent.OnTapAlarmStatusButton ->
                        viewModel.onTapAlarmStatusButtonOccurred(event.alarmStatus)

                    is MyPageViewEvent.OnTapBackgroundMusicStatusButton ->
                        viewModel.onTapBackgroundMusicStatusButtonOccurred()

                    is MyPageViewEvent.OnTapBackgroundMusicChangeButton ->
                        viewModel.onTapBackgroundMusicChangeButtonOccurred()

                    is MyPageViewEvent.OnBackgroundMusicChanged ->
                        viewModel.onBackgroundMusicChangedOccurred(event.musicName)

                    is MyPageViewEvent.OnTapChangePasswordButton -> {
                        val intent = Intent(requireContext(), PasswordChangeActivity::class.java)
                        startActivity(intent)
                    }
                    is MyPageViewEvent.OnTapLogOutButton -> {
                        viewModel.onTapLogOutButtonOccurred()
                    }
                    is MyPageViewEvent.OnTapTermsButton -> {}
                    is MyPageViewEvent.OnShowToast -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                    }

                    MyPageViewEvent.OnLogOut -> {
                        navigateToWelcomeActivity()
                    }
                }
            }
        }
    }

    private fun navigateToWelcomeActivity() {
        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun subScribeViewState() {
        viewLifecycleOwner.repeatOnStarted {
            val inputMethodManager = requireContext().getSystemService(
                Context.INPUT_METHOD_SERVICE,
            ) as InputMethodManager
            viewModel.uiState.collect { state ->
                when (state) {
                    is MyPageViewState.Default -> {
                        setBackgroundMusicStatusUI(state)
                        setAlarmStatusUI(state)
                        setDefaultUI(inputMethodManager)
//                            TODO: Marquee 효과 적용이 안되는 중..
                        binding.musicTextView.requestFocus()
                        binding.musicTextView.isSelected = true
                    }

                    is MyPageViewState.NicknameEditState -> {
                        setBackgroundMusicStatusUI(state)
                        setAlarmStatusUI(state)
                        setNicknameEditUI(inputMethodManager)
                        binding.musicTextView.requestFocus()
                        binding.musicTextView.isSelected = true
                    }

                    is MyPageViewState.BackgroundMusicSelectState -> {
                        showBackgroundMusicSelectDialog()
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

        dialogBinding.setMusicSelector(musicList, viewModel.uiState.value.backgroundMusic)
        dialog.setContentView(dialogBinding.root)
        dialog.setOnDismissListener {
            viewModel.onBackgroundMusicChanged(musicList[dialogBinding.musicSelector.value])
        }
        BackgroundMusicPlayer.resumeMusic()
        dialog.show()
    }

    private fun createFullScreenDialog(): Dialog {
        return Dialog(requireContext(), R.style.Base_FTR_FullScreenDialog).apply {
            window?.setBackgroundDrawableResource(R.drawable.btn_white_green_36dp)
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
                BackgroundMusicPlayer.stopMusic()
                BackgroundMusicPlayer.releaseMusicPlayer()
                BackgroundMusicPlayer.playMusic(requireContext(), musicList[selectedNow])
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
