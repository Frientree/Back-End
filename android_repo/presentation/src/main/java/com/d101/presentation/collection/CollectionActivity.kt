package com.d101.presentation.collection

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.d101.domain.model.JuiceForCollection
import com.d101.presentation.R
import com.d101.presentation.collection.adapter.CollectionAdapter
import com.d101.presentation.collection.event.CollectionViewEvent
import com.d101.presentation.collection.state.CollectionViewState
import com.d101.presentation.collection.viewmodel.CollectionViewModel
import com.d101.presentation.databinding.ActivityCollectionBinding
import com.d101.presentation.databinding.DialogJuiceDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import utils.CustomToast
import utils.repeatOnStarted

@AndroidEntryPoint
class CollectionActivity : AppCompatActivity() {
    private val viewModel: CollectionViewModel by viewModels()
    private lateinit var binding: ActivityCollectionBinding
    private lateinit var collectionAdapter: CollectionAdapter
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        collectionAdapter = CollectionAdapter { juice ->
            viewModel.onTapCollectionItem(juice)
        }
        binding.juiceCollectionRecyclerView.adapter = collectionAdapter

        binding.backButtonImageView.setOnClickListener { viewModel.onTapBackButton() }
        subscribeEvent()
        subscribeViewState()
    }

    private fun showJuiceDetailDialog(juice: JuiceForCollection) {
        dialog = createFullScreenDialog()
        val dialogBinding = DialogJuiceDetailBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        Glide.with(dialogBinding.root).load(juice.juiceImageUrl)
            .into(dialogBinding.juiceImageImageView)
        dialogBinding.juiceNameTextView.text = juice.juiceName
        dialogBinding.juiceDescriptionTextView.text = juice.juiceDescription
        if (dialog.isShowing) dialog.dismiss()
        dialog.show()
    }

    private fun createFullScreenDialog(): Dialog {
        return Dialog(this, R.style.Base_FTR_FullScreenDialog).apply {
            window?.setBackgroundDrawableResource(R.drawable.bg_white_radius_30dp)
        }
    }

    private fun showToast(message: String) {
        CustomToast.createAndShow(this, message)
    }

    private fun subscribeEvent() {
        this.repeatOnStarted {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    CollectionViewEvent.Init -> viewModel.onInitOccurred()
                    is CollectionViewEvent.OnShowJuiceDetailDialog -> {
                        showJuiceDetailDialog(event.juice)
                    }

                    is CollectionViewEvent.OnShowToast -> showToast(event.message)

                    CollectionViewEvent.OnTapBackButton -> onBackPressedDispatcher.onBackPressed()
                    is CollectionViewEvent.OnTapCollectionItem ->
                        viewModel.onTapCollectionItemOccurred(event.juice)
                }
            }
        }
    }

    private fun subscribeViewState() {
        this.repeatOnStarted {
            viewModel.uiState.collect { state ->
                when (state) {
                    is CollectionViewState.Default -> {
                        collectionAdapter.submitList(state.juiceList)
                    }
                }
            }
        }
    }
}
