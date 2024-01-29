package com.d101.presentation.main.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentSelectInputTypeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectInputTypeFragment : Fragment() {
    private var _binding: FragmentSelectInputTypeBinding? = null
    private val binding get() = _binding!!

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

        binding.speechLottieView.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.before_fragment_container_view, FruitCreationBySpeechFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.textTextView.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.before_fragment_container_view, FruitCreationByTextFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
