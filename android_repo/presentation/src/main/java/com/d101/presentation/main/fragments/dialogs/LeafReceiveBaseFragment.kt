package com.d101.presentation.main.fragments.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentLeafReceiveBaseBinding
import com.d101.presentation.main.event.LeafReceiveEvent
import com.d101.presentation.main.viewmodel.LeafReceiveViewModel
import dagger.hilt.android.AndroidEntryPoint
import utils.repeatOnStarted

@AndroidEntryPoint
class LeafReceiveBaseFragment : DialogFragment() {

    private var _binding: FragmentLeafReceiveBaseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LeafReceiveViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_leaf_receive_base,
            container,
            false,
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        collectEvent()
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.leafEventFlow.collect { event ->
                when (event) {
                    is LeafReceiveEvent.ShakingLeafPage -> navigateToTargetFragment(
                        LeafReceiveFragment(),
                    )

                    is LeafReceiveEvent.ReadyToReceive -> {
                        navigateToTargetFragment(LeafReceivedFragment())
                    }

                    is LeafReceiveEvent.ReportLeafComplete -> {
                        Toast.makeText(activity, "성공적으로 신고되었습니다.", Toast.LENGTH_SHORT).show()
                        LeafDialogInterface.dialog.dismiss()
                    }
                    is LeafReceiveEvent.ShowErrorToast -> {
                        Toast.makeText(activity, event.message, Toast.LENGTH_SHORT).show()
                        LeafDialogInterface.dialog.dismiss()
                    }
                }
            }
        }
    }

    private fun navigateToTargetFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.leaf_fragment_container_view, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
