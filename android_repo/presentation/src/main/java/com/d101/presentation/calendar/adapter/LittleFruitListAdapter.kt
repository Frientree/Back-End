package com.d101.presentation.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d101.presentation.R
import com.d101.presentation.databinding.ItemLittleFruitBinding

typealias LittleFruitImageUrl = String

class LittleFruitListAdapter() :
    ListAdapter<Pair<LittleFruitImageUrl, Int>, LittleFruitListAdapter.LittleFruitViewHolder>(
        diffUtil,
    ) {

    inner class LittleFruitViewHolder(private val binding: ItemLittleFruitBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fruitPair: Pair<LittleFruitImageUrl, Int>) {
            binding.littleFruitImageImageView.setImageResource(R.drawable.tree1)
            binding.fruitCountTextView.text = fruitPair.second.toString()
            Glide.with(this.itemView).load(fruitPair.first).into(binding.littleFruitImageImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LittleFruitViewHolder {
        val binding =
            ItemLittleFruitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LittleFruitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LittleFruitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Pair<LittleFruitImageUrl, Int>>() {
            override fun areItemsTheSame(
                oldItem: Pair<LittleFruitImageUrl, Int>,
                newItem: Pair<LittleFruitImageUrl, Int>,
            ): Boolean {
                return oldItem.first == newItem.first && oldItem.second == newItem.second
            }

            override fun areContentsTheSame(
                oldItem: Pair<LittleFruitImageUrl, Int>,
                newItem: Pair<LittleFruitImageUrl, Int>,
            ): Boolean {
                return oldItem.first == newItem.first && oldItem.second == newItem.second
            }
        }
    }
}
