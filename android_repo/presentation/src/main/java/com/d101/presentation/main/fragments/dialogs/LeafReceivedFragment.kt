package com.d101.presentation.main.fragments.dialogs

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentLeafReceivedBinding
import com.d101.presentation.main.viewmodel.LeafReceiveViewModel

class LeafReceivedFragment : Fragment() {

    private var _binding: FragmentLeafReceivedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LeafReceiveViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_leaf_received,
            container,
            false,
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.leafDescriptionTextView.text = viewModel.leaf.leafContent
        // 다이얼로그에서 처리하기
        binding.reportButton.setOnClickListener {
            reportDialog()
        }
    }

    private fun reportDialog() {
        // AlertDialog.Builder를 사용하여 AlertDialog 생성
        val builder = AlertDialog.Builder(context)
        // 커스텀 레이아웃을 AlertDialog에 설정
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.report_alert_dialog, null)
        builder.setView(view)

        // AlertDialog 생성
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 버튼 리스너 설정
        view.findViewById<Button>(R.id.negativeButton).setOnClickListener {
            // "취소" 버튼 클릭 시 할 작업
            alertDialog.dismiss()
        }
        view.findViewById<Button>(R.id.positiveButton).setOnClickListener {
            // "확인" 버튼 클릭 시 할 작업
            viewModel.onReportLeaf()
            alertDialog.dismiss()
        }

        // AlertDialog 표시
        alertDialog.show()
    }
}
