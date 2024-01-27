package com.d101.presentation.main.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentFruitCreateByTextBinding

class FruitCreateByTextFragment : Fragment() {
    private var _binding: FragmentFruitCreateByTextBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fruit_create_by_text, container, false)
//        _binding = FragmentFruitCreateByTextBinding.inflate(inflater)
//        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
