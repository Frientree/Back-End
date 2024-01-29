package com.d101.presentation.main.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        return inflater.inflate(R.layout.fragment_select_input_type, container, false)
        /**
         * view Binding을 사용하고 싶었으나 다이얼로그 크기가 원하는대로 지정되지 않아 일단 findviewbyid사용하는 방식으로 지정
         * 추후 리팩토링 예정
         * */
        //        _binding = FragmentSelectInputTypeBinding.inflate(inflater)
        //        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.text_text_view).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.before_fragment_container_view, FruitCreationByTextFragment())
                .addToBackStack(null)
                .commit()
        }
        /**
         * view Binding을 사용하고 싶었으나 다이얼로그 크기가 원하는대로 지정되지 않아 일단 findviewbyid사용하는 방식으로 지정
         * 추후 리팩토링 예정
         * */
//        binding.textTextView.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .add(R.id.before_fragment_container_view, FruitCreateByTextFragment())
//                .addToBackStack(null)
//                .commit()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
