package com.d101.presentation.main.fragments.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentLeafReceiveBinding
import com.d101.presentation.main.MainActivity
import com.d101.presentation.main.viewmodel.LeafReceiveViewModel
import utils.ShakeEventListener
import utils.ShakeSensorModule

class LeafReceiveFragment : Fragment() {

    private var _binding: FragmentLeafReceiveBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LeafReceiveViewModel by viewModels({ requireParentFragment() })

    private lateinit var shakeSensor: ShakeSensorModule

    private lateinit var activity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_leaf_receive,
            container,
            false,
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shakeTree()
        changeChip()
    }
    private fun changeChip() {
        var lastCheckedId = binding.leafCategoryChipGroup.checkedChipId
        binding.leafCategoryChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                binding.leafCategoryChipGroup.check(lastCheckedId)
            } else {
                when (checkedIds[0]) {
                    R.id.leaf_category_consolation_chip -> {
                        viewModel.checkedChipId = 0
                        lastCheckedId = checkedIds[0]
                        binding.leafCategoryConsolationChip.chipIcon = ContextCompat.getDrawable(
                            activity,
                            R.drawable.img_leaf_category_chip,
                        )
                        binding.leafCategoryCheeringChip.chipIcon = null
                        binding.leafCategoryFreeChip.chipIcon = null
                    }

                    R.id.leaf_category_cheering_chip -> {
                        viewModel.checkedChipId = 1
                        lastCheckedId = checkedIds[0]
                        binding.leafCategoryConsolationChip.chipIcon = null
                        binding.leafCategoryCheeringChip.chipIcon = ContextCompat.getDrawable(
                            activity,
                            R.drawable.img_leaf_category_chip,
                        )
                        binding.leafCategoryFreeChip.chipIcon = null
                    }

                    R.id.leaf_category_free_chip -> {
                        viewModel.checkedChipId = 2
                        lastCheckedId = checkedIds[0]
                        binding.leafCategoryConsolationChip.chipIcon = null
                        binding.leafCategoryCheeringChip.chipIcon = null
                        binding.leafCategoryFreeChip.chipIcon = ContextCompat.getDrawable(
                            activity,
                            R.drawable.img_leaf_category_chip,
                        )
                    }
                }
            }
        }
    }

    private fun shakeTree() {
        val progressBar =
            binding.leafReceiveProgressBar

        shakeSensor = ShakeSensorModule(
            requireContext(),
            object : ShakeEventListener {
                override fun onShakeSensed() {
                    if (progressBar.progress < progressBar.max) {
                        progressBar.progress += 3
                    }

                    if (progressBar.progress >= progressBar.max) {
                        progressBar.progress = progressBar.max
                        shakeSensor.stop()
                        viewModel.onReadyToReceive()
                    }
                }
            },
        )

        shakeSensor.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shakeSensor.stop()
        _binding = null
    }
}
