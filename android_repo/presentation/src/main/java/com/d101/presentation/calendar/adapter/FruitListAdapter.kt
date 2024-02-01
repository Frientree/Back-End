package com.d101.presentation.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d101.domain.model.Fruit
import com.d101.presentation.databinding.ItemFruitBinding
import utils.toMonthDayFormat

class FruitListAdapter() :
    ListAdapter<Fruit, FruitListAdapter.FruitViewHolder>(diffUtil) {

    inner class FruitViewHolder(private val binding: ItemFruitBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fruit: Fruit) {
            binding.fruitNameTextView.text = fruit.name
            binding.emotionTextView.text = fruit.emotion
            binding.dateTextView.text = fruit.date.toMonthDayFormat()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitViewHolder {
        val binding = ItemFruitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FruitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FruitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Fruit>() {
            override fun areItemsTheSame(oldItem: Fruit, newItem: Fruit): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: Fruit, newItem: Fruit): Boolean {
                return oldItem == newItem
            }
        }
    }
}
