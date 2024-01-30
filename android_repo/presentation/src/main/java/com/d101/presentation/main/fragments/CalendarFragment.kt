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
import utils.repeatOnStarted

class CalendarFragment : Fragment() {
    private val viewModel: CalendarViewModel by viewModels()
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

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
        viewModel.init()
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
//                        TODO(지워야 한다)
                        val list = makeDummyList()
                        fruitListAdapter.submitList(list)
                        val countList = countFruits(list)
                        val littleFruitListAdapter = LittleFruitListAdapter()
                        binding.littleFruitListRecyclerView.adapter = littleFruitListAdapter
                        littleFruitListAdapter.submitList(countList)
                        binding.fruitListLinearLayout.gravity = Gravity.CENTER_VERTICAL
                        setJuiceCreatableStatusView(state)
                        setTodayFruitStatisticsView(state)
                    }

                    is CalendarViewState.JuicePresentState -> {
                        binding.juiceOfWeekTextView.visibility = View.VISIBLE
                        binding.juiceOfWeekInfoConstraintLayout.visibility = View.VISIBLE
                        binding.juiceReadyTextView.visibility = View.GONE
                        binding.notEnoughFruitsTextView.visibility = View.GONE
                        binding.juiceRequirementsTextView.visibility = View.GONE
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

    private fun makeDummyList(): ArrayList<Fruit> {
        val list = ArrayList<Fruit>()
        val fruits = arrayOf(
            "사과",
            "딸기",
            "레몬",
            "애플망고",
            "키위",
            "체리",
            "블루베리",
            "포도",
        )

        fruits.forEach { fruitName ->
            list.add(
                Fruit(
                    2024L,
                    2035L,
                    fruitName,
                    "사과는 맛있다",
                    "https://www.naver.com",
                    "사과는 맛있다",
                    "행복",
                    5,
                ),
            )
        }
        return list
    }

    private fun showShakeJuiceDialog() {
        val dialog = createFullScreenDialog()
        val dialogBinding = DialogJuiceShakeBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setOnCancelListener {
            viewModel.onCancelJuiceShakeDialog()
        }
        dialog.show()
    }

    private fun createFullScreenDialog(): Dialog {
        return Dialog(requireContext(), R.style.FullScreenDialogStyle).apply {
            window?.setBackgroundDrawableResource(R.drawable.bg_white_radius_30dp)
        }
    }

    private fun countFruits(fruits: ArrayList<Fruit>): List<Pair<String, Int>> {
        val counts = mutableMapOf<String, Int>()

        fruits.forEach { fruit ->
            val key = fruit.name
            counts[key] = counts.getOrDefault(key, 0) + 1
        }

        return counts.map { (key, count) -> Pair(key, count) }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
