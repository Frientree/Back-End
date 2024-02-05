package com.d101.presentation.collection

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d101.domain.model.JuiceForCollection
import com.d101.presentation.databinding.ItemCollectionBinding

class CollectionAdapter(private val collectionClickListener: ((JuiceForCollection) -> Unit)) :
    ListAdapter<JuiceForCollection, CollectionAdapter.CollectionViewHolder>(diffUtil) {
    class CollectionViewHolder(
        private val binding: ItemCollectionBinding,
        private val itemClickListener: (JuiceForCollection) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(juice: JuiceForCollection) {
            binding.juiceNameTextView.text = juice.juiceName
            Glide.with(itemView).load(juice.juiceImageUrl).into(binding.juiceImageImageView)
            if (juice.juiceOwn) {
                binding.collectionItemLinearLayout.setOnClickListener { itemClickListener(juice) }
            } else {
                binding.juiceImageImageView.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding =
            ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionViewHolder(binding, collectionClickListener)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<JuiceForCollection>() {
            override fun areItemsTheSame(
                oldItem: JuiceForCollection,
                newItem: JuiceForCollection,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: JuiceForCollection,
                newItem: JuiceForCollection,
            ): Boolean {
                return oldItem.juiceNum == newItem.juiceNum
            }
        }
    }
}
