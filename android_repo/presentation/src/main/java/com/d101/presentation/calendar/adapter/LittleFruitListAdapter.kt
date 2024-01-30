package com.d101.presentation.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d101.presentation.R
import com.d101.presentation.databinding.ItemLittleFruitBinding

class LittleFruitListAdapter() :
    ListAdapter<Pair<String, Int>, LittleFruitListAdapter.LittleFruitViewHolder>(diffUtil) {

    inner class LittleFruitViewHolder(private val binding: ItemLittleFruitBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fruitPair: Pair<String, Int>) {
            binding.littleFruitImageImageView.setImageResource(R.drawable.tree1)
            binding.fruitCountTextView.text = fruitPair.second.toString()
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
        private val diffUtil = object : DiffUtil.ItemCallback<Pair<String, Int>>() {
            override fun areItemsTheSame(
                oldItem: Pair<String, Int>,
                newItem: Pair<String, Int>,
            ): Boolean {
                return oldItem.first == newItem.first && oldItem.second == newItem.second
            }

            override fun areContentsTheSame(
                oldItem: Pair<String, Int>,
                newItem: Pair<String, Int>,
            ): Boolean {
                return oldItem.first == newItem.first && oldItem.second == newItem.second
            }
        }
    }
}
