package com.d101.presentation.main.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.domain.model.Fruit
import com.d101.presentation.R
import com.d101.presentation.calendar.adapter.FruitListAdapter
import com.d101.presentation.calendar.adapter.LittleFruitListAdapter
import com.d101.presentation.calendar.event.CalendarViewEvent
import com.d101.presentation.calendar.state.CalendarViewState
import com.d101.presentation.calendar.state.JuiceCreatableStatus
import com.d101.presentation.calendar.state.TodayFruitCreationStatus
import com.d101.presentation.calendar.viewmodel.CalendarViewModel
import com.d101.presentation.databinding.DialogJuiceShakeBinding
import com.d101.presentation.databinding.FragmentCalendarBinding
import dagger.hilt.android.AndroidEntryPoint
import utils.ShakeEventListener
import utils.ShakeSensorModule
import utils.repeatOnStarted

@AndroidEntryPoint
class CalendarFragment : Fragment() {
    private val viewModel: CalendarViewModel by viewModels()
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var shakeSensor: ShakeSensorModule

    private lateinit var dialog: Dialog

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

                    CalendarViewEvent.OnCancelJuiceShake -> {
                        viewModel.onCancelJuiceShakeOccurred()
                    }

                    is CalendarViewEvent.OnSetMonth -> {
                        viewModel.onMonthChangedOccurred(2024010120240131)
                    }

                    CalendarViewEvent.OnSetWeek -> {
                        viewModel.onWeekChangeOccurred(2024010920240115)
                    }
                }
            }
        }
    }

    private fun subscribeViewState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collect { state ->
                when (state) {
                    is CalendarViewState.JuiceAbsentState -> {
                        binding.juiceOfWeekTextView.visibility = View.GONE
                        binding.juiceOfWeekInfoConstraintLayout.visibility = View.GONE
                        val fruitListAdapter = FruitListAdapter()
                        binding.fruitListRecyclerView.adapter = fruitListAdapter
                        fruitListAdapter.submitList(state.fruitListForWeek)
                        val countList = countFruits(state.fruitListForWeek)
                        val littleFruitListAdapter = LittleFruitListAdapter()
                        binding.littleFruitListRecyclerView.adapter = littleFruitListAdapter
                        littleFruitListAdapter.submitList(countList)
                        binding.fruitListLinearLayout.gravity = Gravity.CENTER_VERTICAL
                        setJuiceCreatableStatusView(state)
                        setTodayFruitStatisticsView(state)
                    }

                    is CalendarViewState.JuicePresentState -> {
                        if (::dialog.isInitialized && dialog.isShowing) dialog.dismiss()
                        binding.juiceOfWeekTextView.visibility = View.VISIBLE
                        binding.juiceOfWeekInfoConstraintLayout.visibility = View.VISIBLE
                        binding.juiceMakingButtonLinearLayout.visibility = View.GONE
                        binding.juiceReadyTextView.visibility = View.GONE
                        binding.notEnoughFruitsTextView.visibility = View.GONE
                        binding.juiceRequirementsTextView.visibility = View.GONE
                        binding.juiceGraph.setFruitList(state.fruitListForWeek)
                        setTodayFruitStatisticsView(state)
                    }

                    is CalendarViewState.JuiceShakeState -> {
                        showShakeJuiceDialog()
                    }
                }
            }
        }
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
                        progressBar.incrementProgressBy(
                            10,
                        )
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

        dialog.setOnCancelListener {
            viewModel.onCancelJuiceShakeDialog()
        }

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

    private fun countFruits(fruits: List<Fruit>): List<Pair<String, Int>> {
        val counts = mutableMapOf<String, Int>()

        fruits.forEach { fruit ->
            val key = fruit.name
            counts[key] = counts.getOrDefault(key, 0) + 1
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
