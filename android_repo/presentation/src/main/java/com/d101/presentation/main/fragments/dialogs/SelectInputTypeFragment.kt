package com.d101.presentation.main.fragments.dialogs

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentSelectInputTypeBinding
import com.d101.presentation.main.MainActivity
import com.d101.presentation.main.state.CreateFruitDialogViewEvent
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectInputTypeFragment : Fragment() {
    private var _binding: FragmentSelectInputTypeBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity: MainActivity

    private var isTextInput = true

    private val viewModel: FruitCreateViewModel by viewModels({ requireParentFragment() })

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
            R.layout.fragment_select_input_type,
            container,
            false,
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.speechLottieView.setOnClickListener {
            requestAudioPermission()
        }
        binding.textTextView.setOnClickListener {
            isTextInput = true
            moveToFragment()
        }
    }

    private fun moveToFragment() {
        if (isTextInput) {
            viewModel.changeViewEvent(CreateFruitDialogViewEvent.FruitCreationByTextViewEvent)
        } else {
            viewModel.changeViewEvent(CreateFruitDialogViewEvent.FruitCreationBySpeechViewEvent)
        }
    }

    private fun requestAudioPermission() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.RECORD_AUDIO,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Requesting permission to RECORD_AUDIO
            val permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
            requestPermissionLauncher.launch(permissions)
        } else {
            isTextInput = false
            moveToFragment()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        permissions.entries.forEach { (permission, granted) ->
            if (granted) {
                // 권한이 허용된 경우
                isTextInput = false
                moveToFragment()
            } else {
                // 권한이 거부된 경우
                if (shouldShowRequestPermissionRationale(permission)) {

                    FruitDialogInterface.dialog.dismiss()
                    Toast.makeText(activity, "권한을 허용해야 말하기 기능을 이용할 수 있습니다.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // '다시 보지 않기' 선택 또는 기타 상황 처리
                    Log.i("Permission", "Permission denied without ask again: $permission")
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
