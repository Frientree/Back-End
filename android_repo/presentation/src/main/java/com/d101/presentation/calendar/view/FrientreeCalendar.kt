package com.d101.presentation.calendar.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.d101.presentation.R
import com.d101.presentation.calendar.adapter.FruitInCalendarListAdapter
import com.d101.presentation.databinding.LayoutFrientreeCalendarBinding
import com.d101.presentation.model.FruitInCalendar

class FrientreeCalendar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutFrientreeCalendarBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_frientree_calendar,
            this,
            true,
        )

    private lateinit var adapter: FruitInCalendarListAdapter

    fun setCalendarAdapter(adapter: FruitInCalendarListAdapter) {
        this.adapter = adapter
        binding.calendarRecyclerView.adapter = adapter
    }

    fun submitList(fruitInCalendarList: List<FruitInCalendar>) {
        adapter.submitList(fruitInCalendarList)
    }
}
