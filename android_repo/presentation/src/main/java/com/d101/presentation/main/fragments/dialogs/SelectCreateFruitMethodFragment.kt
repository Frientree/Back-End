package com.d101.presentation.main.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentSelectCreateFruitMethodBinding

class SelectCreateFruitMethodFragment : Fragment() {
    private var _binding: FragmentSelectCreateFruitMethodBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_create_fruit_method, container, false)
//        _binding = FragmentSelectCreateFruitMethodBinding.inflate(inflater)
//        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.text_text_view).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .add(R.id.before_fragment_container_view, FruitCreateByTextFragment())
                .addToBackStack("")
                .commit()
        }
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
