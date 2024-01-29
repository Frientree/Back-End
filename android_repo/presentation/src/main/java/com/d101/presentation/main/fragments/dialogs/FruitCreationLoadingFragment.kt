package com.d101.presentation.main.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import utils.repeatOnStarted

const val INPUT_TYPE = "INPUT_TYPE"

class FruitCreationLoadingFragment : Fragment() {

    private var inputType: String = ""

    private val viewModel: FruitCreateViewModel by viewModels(
        { requireParentFragment() },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            inputType = it.getString(INPUT_TYPE).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_fruit_creation_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (inputType) {
            TEXT -> {
                viewModel.setTodayFruitListByText()
            }

            SPEECH -> {
                // 뷰모델에서 STT 함수 호출
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.todayFruitList.collect {
                // UI state 변경
                if (it.isNotEmpty()) {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.before_fragment_container_view, AfterFruitCreateFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(inputType: String) =
            FruitCreationLoadingFragment().apply {
                arguments = Bundle().apply {
                    putString(INPUT_TYPE, inputType)
                }
            }

        const val TEXT = "text"
        const val SPEECH = "speech"
    }
}
