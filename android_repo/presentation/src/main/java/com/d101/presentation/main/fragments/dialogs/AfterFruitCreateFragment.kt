package com.d101.presentation.main.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.databinding.FragmentAfterFruitCreateBinding
import com.d101.presentation.main.viewmodel.FruitCreateViewModel

class AfterFruitCreateFragment : Fragment() {

    private var _binding: FragmentAfterFruitCreateBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FruitCreateViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAfterFruitCreateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: ViewModel에서 todayFruitList를 가져와 화면에 그린다.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
