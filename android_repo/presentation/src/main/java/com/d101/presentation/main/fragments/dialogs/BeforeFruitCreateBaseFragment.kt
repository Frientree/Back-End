package com.d101.presentation.main.fragments.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentBeforeFruitCreateBaseBinding
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeforeFruitCreateBaseFragment : DialogFragment() {
    private val viewModel: FruitCreateViewModel by viewModels()
    private var _binding: FragmentBeforeFruitCreateBaseBinding? = null
    private val binding get() = _binding!!
    /*
    * 백 버튼을 눌렀을 때 다이얼로그가 꺼지는 것이 아닌 전 프래그먼트로 돌아가는
    * 코드를 구현하고 싶음 ...
    * */
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        requireActivity().onBackPressedDispatcher.addCallback(
//            this,
//            onBackPressedCallback,
//        )
//    }
//
//    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
//        override fun handleOnBackPressed() {
//            val current =
//                childFragmentManager.findFragmentById(binding.beforeFragmentContainerView.id)
//            if (current != null) {
//                childFragmentManager.beginTransaction()
//                    .remove(current)
//                    .commit()
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBeforeFruitCreateBaseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewModel
        childFragmentManager.beginTransaction()
            .replace(R.id.before_fragment_container_view, SelectInputTypeFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
