package com.d101.presentation.calendar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d101.domain.utils.toLocalDate
import com.d101.presentation.R
import com.d101.presentation.databinding.ItemDayInCarlendarBinding
import com.d101.presentation.model.DayMonthType
import com.d101.presentation.model.DaySelectType
import com.d101.presentation.model.FruitInCalendar
import java.time.LocalDate

class FruitInCalendarListAdapter(
    private val weekClick: (LocalDate) -> (Unit),
) : ListAdapter<FruitInCalendar, FruitInCalendarListAdapter.FruitInCalendarViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitInCalendarViewHolder {
        val binding = ItemDayInCarlendarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return FruitInCalendarViewHolder(binding, weekClick)
    }

    override fun onBindViewHolder(holder: FruitInCalendarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FruitInCalendarViewHolder(
        private val binding: ItemDayInCarlendarBinding,
        private val weekClick: (LocalDate) -> (Unit),
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var fruit: FruitInCalendar

        init {
            itemView.setOnClickListener {
                weekClick(fruit.day.toLocalDate())
            }
        }

        fun bind(fruit: FruitInCalendar) {
            this.fruit = fruit
            if (fruit.imageUrl.isEmpty()) {
                binding.dayTextView.text = fruit.day.toLocalDate().dayOfMonth.toString()
                binding.dayTextView.visibility = View.VISIBLE
                binding.dayFruitImageView.visibility = View.INVISIBLE
            } else {
                Glide.with(this.itemView).load(fruit.imageUrl).into(binding.dayFruitImageView)
                binding.dayTextView.visibility = View.INVISIBLE
                binding.dayFruitImageView.visibility = View.VISIBLE
            }

            val backgroundRes = when (fruit.selectType) {
                DaySelectType.START -> R.drawable.bg_selected_week_start
                DaySelectType.MIDDLE -> R.drawable.bg_selected_week
                DaySelectType.END -> R.drawable.bg_selected_week_end
                DaySelectType.NOT_SELECTED -> R.drawable.bg_transparent
            }
            binding.dayConstraintLayout.setBackgroundResource(backgroundRes)

            val fontFamily = when (fruit.selectType) {
                DaySelectType.START, DaySelectType.MIDDLE, DaySelectType.END -> R.font.binggrae_bold
                DaySelectType.NOT_SELECTED -> R.font.binggrae
            }

            val textColorRes = when (fruit.monthType) {
                DayMonthType.THIS_MONTH -> R.color.black
                DayMonthType.NOT_THIS_MONTH -> R.color.gray
            }
            binding.dayTextView.setTextColor(binding.root.context.getColor(textColorRes))
            binding.dayTextView.typeface = binding.root.context.resources.getFont(fontFamily)
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
