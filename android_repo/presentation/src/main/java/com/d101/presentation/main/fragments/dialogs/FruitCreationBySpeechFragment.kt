package com.d101.presentation.main.fragments.dialogs

import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentFruitCreationBySpeechBinding
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer

class FruitCreationBySpeechFragment : Fragment() {

    private var _binding: FragmentFruitCreationBySpeechBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FruitCreateViewModel by viewModels({ requireParentFragment() })
    private lateinit var audioFile: File
    private lateinit var secText: String

    private var recorder: MediaRecorder? = null
    private lateinit var timerTask: Timer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_fruit_creation_by_speech,
            container,
            false,
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isTextInput = false
        secText = getString(R.string.speech_second_text)
        startRecording()

        binding.createFruitBySpeechButton.setOnClickListener {
            createFruitBySpeech()
        }
    }

    private fun createFruitBySpeech() {
        timerTask.cancel()
        stopRecording()
        viewModel.setAudioFile(audioFile)
        viewModel.onGoFruitLoadingView()
    }

    private fun startRecording() {
        audioFile = createAudioFile()
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(requireContext())
        } else {
            MediaRecorder()
        }
        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(audioFile.absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            prepare()

            start()
            startTimer()
        }
    }

    private fun startTimer() {
        var time = 0
        timerTask = kotlin.concurrent.timer(period = 10) {
            time++
            val sec = time / 100

            activity?.runOnUiThread {
                try {
                    binding.speechProgressBar.progress = time
                    binding.speechSecondTextView.text = String.format("%d %s", sec, secText)
                } catch (e: NullPointerException) {
                    timerTask.cancel()
                }
            }
            if (sec >= 30) {
                createFruitBySpeech()
            }
        }
    }

    private fun createAudioFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val audioFileName = "AUDIO_$timeStamp"
        val cacheDir: File = requireContext().cacheDir
        return File.createTempFile(audioFileName, ".m4a", cacheDir)
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null

        timerTask.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
