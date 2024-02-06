package com.d101.presentation.main.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentLeafReceiveBinding
import com.d101.presentation.main.viewmodel.LeafReceiveViewModel

class LeafReceivedFragment : Fragment() {

    private var _binding: FragmentLeafReceiveBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LeafReceiveViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaf_received, container, false)
    }
}
