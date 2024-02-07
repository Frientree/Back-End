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
import com.d101.presentation.databinding.FragmentLeafSendBinding
import com.d101.presentation.main.MainActivity
import com.d101.presentation.main.viewmodel.LeafSendViewModel
import com.d101.presentation.main.state.LeafSendViewState
import dagger.hilt.android.AndroidEntryPoint
import utils.CustomToast
import utils.repeatOnStarted

@AndroidEntryPoint
class LeafMessageToSendFragment : Fragment() {

    private var _binding: FragmentLeafSendBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LeafSendViewModel by viewModels({ requireParentFragment() })

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
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_leaf_send, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setSendButton()
        changeChip()
        collectUiState()
    }

    private fun collectUiState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collect { state ->
                when (state) {
                    is LeafSendViewState.AlreadySendState -> setVisibility()
                    else -> {}
                }
            }
        }
    }

    private fun setVisibility() {
        binding.leafCategoryChipGroup.visibility = View.GONE
        binding.leafTextLayout.visibility = View.GONE
        binding.leafSendButton.visibility = View.GONE
        binding.alreadySendTextView.visibility = View.VISIBLE
        binding.leafLayout.setBackgroundResource(R.color.main_green)
    }

    private fun setSendButton() {
        binding.leafSendButton.setOnClickListener {
            if (binding.leafTextView.text.isNullOrEmpty()) {
                showToast("이파리를 입력해주세요!")
            } else {
                viewModel.onSendLeaf()
            }
        }
    }

    private fun showToast(message: String) = CustomToast.createAndShow(activity, message)
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
}
