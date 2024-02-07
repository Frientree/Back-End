package com.d101.presentation.main.fragments.dialogs

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentLeafBlowingBinding
import com.d101.presentation.main.MainActivity
import com.d101.presentation.main.viewmodel.LeafSendViewModel
import utils.repeatOnStarted
import kotlin.properties.Delegates

class LeafBlowingFragment : Fragment() {
    private var _binding: FragmentLeafBlowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity: MainActivity
    private val viewModel: LeafSendViewModel by viewModels({ requireParentFragment() })

    private var isRecording = false
    private val sampleRate = 44100 // 샘플링 레이트
    private var audioRecord: AudioRecord? = null

    private var bufferSize by Delegates.notNull<Int>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_leaf_blowing,
            container,
            false,
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        startRecording()

        viewLifecycleOwner.repeatOnStarted {
            viewModel.blowing.collect {
                binding.leafBlowingProgressBar.progress += it
                if (binding.leafBlowingProgressBar.progress >= binding.leafBlowingProgressBar.max) {
                    isRecording = false
                    binding.leafBlowingProgressBar.progress = binding.leafBlowingProgressBar.max
                    sendLeaf()
                }
            }
        }
    }

    private fun setListener() {
        val buffer = ByteArray(bufferSize)
        isRecording = true

        audioRecord?.setRecordPositionUpdateListener(object :
            AudioRecord.OnRecordPositionUpdateListener {

            override fun onPeriodicNotification(recorder: AudioRecord) {
                if (isRecording) {
                    // 여기서 오디오 데이터를 처리
                    val readSize = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    val volume = calculateVolume(buffer, readSize)

                    if (volume > 43) { // 임계값 설정
                        isRecording = false
                        viewModel.onBlowing()
                        binding.leafBlowingLottieView.playAnimation()
                        stopRecording()
                    }
                }
            }

            override fun onMarkerReached(recorder: AudioRecord) {
                // 특정 지점에 도달했을 때 호출됩니다.
            }
        })

        audioRecord?.positionNotificationPeriod = 1024 // 콜백이 호출될 주기를 설정
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startRecording() {
        bufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_DEFAULT,
            AudioFormat.ENCODING_PCM_16BIT,
        )

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.RECORD_AUDIO,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
            requestPermissionLauncher.launch(permissions)
        } else {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
            )

            audioRecord?.startRecording()

            setListener()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        permissions.entries.forEach { (permission, granted) ->
            if (!granted) {
                // 권한이 거부된 경우
                if (shouldShowRequestPermissionRationale(permission)) {
                    viewModel.onNeedPermission("권한을 허용해야 보내기 기능을 이용할 수 있습니다.")
                } else {
                    // '다시 보지 않기' 선택 또는 기타 상황 처리
                    Log.i("Permission", "Permission denied without ask again: $permission")
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
                    viewModel.onNeedPermission("권한을 허용해야 보내기 기능을 이용할 수 있습니다.")
                    startActivity(intent)
                }
            } else {
                startRecording()
                setListener()
            }
        }
    }

    private fun stopRecording() {
        audioRecord?.setRecordPositionUpdateListener(null)
        isRecording = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    private fun sendLeaf() {
        stopRecording()
        viewModel.onReadyToSend()
    }

    private fun calculateVolume(buffer: ByteArray, readSize: Int): Int {
        var sum = 0
        for (i in 0 until readSize) {
            sum += Math.abs(buffer[i].toInt())
        }
        if (readSize == 0) return 0
        return sum / readSize
    }
}
