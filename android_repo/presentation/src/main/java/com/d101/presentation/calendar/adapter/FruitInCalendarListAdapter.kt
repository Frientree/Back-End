package com.d101.presentation.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d101.presentation.databinding.ItemDayInCarlendarBinding
import com.d101.presentation.model.FruitInCalendar

class FruitInCalendarListAdapter :
    ListAdapter<FruitInCalendar, FruitInCalendarListAdapter.FruitInCalendarViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitInCalendarViewHolder {
        val binding =
            ItemDayInCarlendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FruitInCalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FruitInCalendarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FruitInCalendarViewHolder(private val binding: ItemDayInCarlendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fruit: FruitInCalendar) {
            binding.dayTextView.text = fruit.day
            Glide.with(this.itemView).load(fruit.imageUrl).into(binding.dayFruitImageView)
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<FruitInCalendar>() {
            override fun areItemsTheSame(
                oldItem: FruitInCalendar,
                newItem: FruitInCalendar,
            ): Boolean {
                return oldItem.day == newItem.day
            }

            override fun areContentsTheSame(
                oldItem: FruitInCalendar,
                newItem: FruitInCalendar,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
