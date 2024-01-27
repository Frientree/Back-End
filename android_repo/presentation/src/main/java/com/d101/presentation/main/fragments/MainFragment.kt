package com.d101.presentation.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentMainBinding
import com.d101.presentation.main.fragments.dialogs.BeforeFruitCreateBaseFragment
import com.d101.presentation.main.viewmodel.MainFragmentViewModel

class MainFragment : Fragment() {
    private val viewModel: MainFragmentViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = viewModel

        viewModel.initTodayDate()

        binding.createFruitButton.setOnClickListener {
            val dialog = BeforeFruitCreateBaseFragment()
            dialog.show(childFragmentManager, "")
        }
    }
// 데이터 바인딩 실패  ...
//    fun invokeDialog(view: View){
//        Log.d("SSAFY", "들어왓슈")
//        val dialog = BeforeFruitCreateBaseFragment()
//        dialog.show(childFragmentManager, "")
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
