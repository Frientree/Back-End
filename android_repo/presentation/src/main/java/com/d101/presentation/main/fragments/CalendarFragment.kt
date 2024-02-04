package com.d101.presentation.main.fragments

import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.d101.domain.model.Fruit
import com.d101.domain.utils.FruitEmotion
import com.d101.presentation.R
import com.d101.presentation.calendar.adapter.FruitInCalendarListAdapter
import com.d101.presentation.calendar.adapter.FruitListAdapter
import com.d101.presentation.calendar.adapter.LittleFruitImageUrl
import com.d101.presentation.calendar.adapter.LittleFruitListAdapter
import com.d101.presentation.calendar.event.CalendarViewEvent
import com.d101.presentation.calendar.state.CalendarViewState
import com.d101.presentation.calendar.state.JuiceCreatableStatus
import com.d101.presentation.calendar.state.TodayFruitCreationStatus
import com.d101.presentation.calendar.viewmodel.CalendarViewModel
import com.d101.presentation.databinding.DialogFruitDetailBinding
import com.d101.presentation.databinding.DialogJuiceShakeBinding
import com.d101.presentation.databinding.FragmentCalendarBinding
import com.d101.presentation.fruit_elements.FruitResources
import com.d101.presentation.mapper.CalendarMapper.toFruitInCalendar
import dagger.hilt.android.AndroidEntryPoint
import utils.ShakeEventListener
import utils.ShakeSensorModule
import utils.darkenColor
import utils.repeatOnStarted

@AndroidEntryPoint
class CalendarFragment : Fragment() {
    private val viewModel: CalendarViewModel by viewModels()
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var shakeSensor: ShakeSensorModule

    private lateinit var dialog: Dialog

    private lateinit var fruitListAdapter: FruitListAdapter
    private lateinit var littleFruitListAdapter: LittleFruitListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        subscribeEvent()
        subscribeViewState()
        fruitListAdapter = FruitListAdapter{fruit -> viewModel.onTapFruitDetailButton(fruit)}
        littleFruitListAdapter = LittleFruitListAdapter()
        binding.frientreeCalendar.setCalendarAdapter(
            FruitInCalendarListAdapter {
                viewModel.onWeekSelected(it)
            },
        )

        binding.frientreeCalendar.setOnMonthClickListener {
            viewModel.onClickNextMonth()
        }

        binding.frientreeCalendar.setOnPrevMonthClickListener {
            viewModel.onClickPreviousMonth()
        }
    }

    private fun setBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    private fun subscribeEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is CalendarViewEvent.Init -> {
                        viewModel.onInitOccurred()
                    }

                    is CalendarViewEvent.OnCompleteJuiceShake -> {
                        viewModel.onCompleteJuiceShakeOccurred()
                    }

                    is CalendarViewEvent.OnTapCollectionButton -> {}
                    is CalendarViewEvent.OnTapJuiceMakingButton -> {
                        viewModel.onTapJuiceMakingButtonOccurred()
                    }

                    is CalendarViewEvent.OnSetMonth -> {
                        viewModel.onMonthChangedOccurred(event.monthDate)
                    }

                    is CalendarViewEvent.OnSetWeek -> {
                        viewModel.onWeekChangeOccurred(event.weekDate)
                    }

                    CalendarViewEvent.OnShowJuiceShakeDialog -> showShakeJuiceDialog()
                    is CalendarViewEvent.OnShowFruitDetailDialog -> showFruitDetailDialog(
                        event.fruit,
                    )

                    is CalendarViewEvent.OnTapFruitDetailButton -> {
                        viewModel.onTapFruitDetailButtonOccurred(event.fruit)
                    }

                    is CalendarViewEvent.OnShowToast -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun subscribeViewState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collect { state ->
                binding.frientreeCalendar.submitList(
                    state.fruitListForMonth.map {
                        it.toFruitInCalendar(state.selectedWeek, state.nowDate.monthValue)
                    },
                )
                binding.frientreeCalendar.setNowDate(state.nowDate)
                when (state) {
                    is CalendarViewState.JuiceAbsentState -> {
                        setViewsVisibility(isJuicePresent = false)
                        updateFruitListView(state)
                        setJuiceCreatableStatusView(state)
                        setTodayFruitStatisticsView(state)
                    }

                    is CalendarViewState.JuicePresentState -> {
                        if (::dialog.isInitialized && dialog.isShowing) dialog.dismiss()
                        setViewsVisibility(isJuicePresent = true)
                        binding.juiceGraph.setFruitList(state.juice.fruitList)
                        updateFruitListView(state)
                        setTodayFruitStatisticsView(state)
                    }
                }
            }
        }
    }

    private fun setViewsVisibility(isJuicePresent: Boolean) {
        binding.juiceOfWeekTextView.visibility = if (isJuicePresent) View.VISIBLE else View.GONE
        binding.juiceOfWeekInfoConstraintLayout.visibility =
            if (isJuicePresent) View.VISIBLE else View.GONE
        binding.juiceMakingButtonLinearLayout.visibility =
            if (isJuicePresent) View.GONE else View.VISIBLE
        binding.juiceReadyTextView.visibility = if (isJuicePresent) View.GONE else View.VISIBLE
        binding.notEnoughFruitsTextView.visibility = if (isJuicePresent) View.GONE else View.VISIBLE
        binding.juiceRequirementsTextView.visibility =
            if (isJuicePresent) View.GONE else View.VISIBLE
    }

    private fun updateFruitListView(state: CalendarViewState) {
        binding.fruitListRecyclerView.adapter = fruitListAdapter
        fruitListAdapter.submitList(state.fruitListForWeek)
        val countList = countFruits(state.fruitListForWeek)
        binding.littleFruitListRecyclerView.adapter = littleFruitListAdapter
        littleFruitListAdapter.submitList(countList)
        binding.fruitListLinearLayout.gravity = Gravity.CENTER_VERTICAL
    }

    private fun setTodayFruitStatisticsView(state: CalendarViewState) {
        when (state.todayFruitCreationStatus) {
            TodayFruitCreationStatus.Created -> {
                binding.todayFruitStatisticsTextView.visibility = View.VISIBLE
            }

            TodayFruitCreationStatus.NotCreated -> {
                binding.todayFruitStatisticsTextView.visibility = View.GONE
            }
        }
    }

    private fun setJuiceCreatableStatusView(state: CalendarViewState.JuiceAbsentState) {
        when (state.juiceCreatableStatus) {
            JuiceCreatableStatus.JuiceCreatable -> {
                binding.juiceReadyTextView.visibility = View.VISIBLE
                binding.notEnoughFruitsTextView.visibility = View.GONE
                binding.juiceRequirementsTextView.visibility = View.GONE
                binding.juiceMakingButtonLinearLayout.isClickable = true
                binding.juiceMakingButtonTextView.setTextColor(
                    resources.getColor(R.color.black, null),
                )
            }

            JuiceCreatableStatus.JuiceUnCreatable -> {
                binding.juiceReadyTextView.visibility = View.GONE
                binding.notEnoughFruitsTextView.visibility = View.VISIBLE
                binding.juiceRequirementsTextView.visibility = View.VISIBLE
                binding.juiceMakingButtonLinearLayout.isClickable = false
                binding.juiceMakingButtonTextView.setTextColor(
                    resources.getColor(R.color.light_gray, null),
                )
            }
        }
    }

    private fun showFruitDetailDialog(fruit: Fruit) {
        dialog = createFullScreenDialog()
        val dialogBinding = DialogFruitDetailBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        Glide.with(dialogBinding.root).load(fruit.imageUrl).into(dialogBinding.fruitImageView)
        dialogBinding.fruitNameTextView.text = fruit.name
        dialogBinding.fruitDescriptionTextView.text = fruit.description
        FruitResources.entries.find { it.fruitEmotion == fruit.fruitEmotion }
            ?.let { fruitResources ->
                val backgroundColor = resources.getColor(fruitResources.color, null)
                dialogBinding.fruitDescriptionCardView.setCardBackgroundColor(
                    backgroundColor,
                )

                dialogBinding.fruitDescriptionCardView.strokeColor =
                    backgroundColor.darkenColor()

                Glide.with(requireContext())
                    .asGif()
                    .load(fruitResources.fallingImage)
                    .into(dialogBinding.fruitDetailBackgroundImageView)
                dialog.show()
            }
    }

    private fun showShakeJuiceDialog() {
        dialog = createFullScreenDialog()
        val dialogBinding = DialogJuiceShakeBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        val progressBar =
            dialogBinding.shakeProgressBarLinearProgressIndicator

        shakeSensor = ShakeSensorModule(
            requireContext(),
            object : ShakeEventListener {
                override fun onShakeSensed() {
                    if (progressBar.progress < progressBar.max) {
                        val progressAnimator =
                            ObjectAnimator.ofInt(
                                progressBar,
                                "progress",
                                progressBar.progress,
                                progressBar.progress + 34,
                            )
                        progressAnimator.duration = 300
                        progressAnimator.interpolator = LinearInterpolator()
                        progressAnimator.start()
                    }

                    if (progressBar.progress >= progressBar.max) {
                        progressBar.progress = progressBar.max
                        shakeSensor.stop()
                        viewModel.onCompleteJuiceShakeOccurred()
                    }
                }
            },
        )

        shakeSensor.start()

        dialog.setOnDismissListener {
            shakeSensor.stop()
        }

        dialog.show()
    }

    private fun createFullScreenDialog(): Dialog {
        return Dialog(requireContext(), R.style.Base_FTR_FullScreenDialog).apply {
            window?.setBackgroundDrawableResource(R.drawable.bg_white_radius_30dp)
        }
    }

    private fun countFruits(fruits: List<Fruit>): List<Pair<LittleFruitImageUrl, Int>> {
        val counts = mutableMapOf<LittleFruitImageUrl, Int>()

        FruitEmotion.entries.forEach { emotion ->
            fruits.forEach { fruit ->
                if (fruit.fruitEmotion == emotion) {
                    counts[fruit.calendarImageUrl] =
                        counts.getOrDefault(fruit.calendarImageUrl, 0) + 1
                }
            }
        }

        return counts.map { (key, count) -> Pair(key, count) }
    }

    override fun onStop() {
        super.onStop()
        if (::shakeSensor.isInitialized) shakeSensor.stop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
