package com.d101.presentation.main.fragments.dialogs

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentSelectInputTypeBinding
import com.d101.presentation.main.MainActivity
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import utils.CustomToast

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
            viewModel.onGoCreateionByTextView()
        } else {
            viewModel.onGoCreateionBySpeechView()
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
                isTextInput = false
                moveToFragment()
            } else {
                if (shouldShowRequestPermissionRationale(permission)) {

                    FruitDialogInterface.dialog.dismiss()
                    showToast("권한을 허용해야 말하기 기능을 이용할 수 있습니다.")
                } else {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            }
        }
    }

    private fun showToast(message: String) =
        CustomToast.createAndShow(requireContext(), message)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
