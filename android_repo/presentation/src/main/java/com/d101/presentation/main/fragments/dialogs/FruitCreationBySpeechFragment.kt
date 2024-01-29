package com.d101.presentation.main.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentFruitCreationBySpeechBinding

class FruitCreationBySpeechFragment : Fragment() {

    private var _binding: FragmentFruitCreationBySpeechBinding? = null
    private val binding get() = _binding!!
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
}
