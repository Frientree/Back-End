package com.d101.presentation.main.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d101.presentation.R
import com.d101.presentation.databinding.FragmentFruitCreationByTextBinding
import com.d101.presentation.main.viewmodel.FruitCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import utils.repeatOnStarted

@AndroidEntryPoint
class FruitCreationByTextFragment : Fragment() {
    private val viewModel: FruitCreateViewModel by viewModels(
        { requireParentFragment() },
    )
    private var _binding: FragmentFruitCreationByTextBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_fruit_creation_by_text, container, false)
        /**
         * view binding을 사용하고자 했으나
         * dialog 크기가 원하는대로 지정되지 않는 이슈로
         * 임시로 find view by id를 사용함.
         * 추후 리팩트링 예정
         * */
        //        _binding = FragmentFruitCreationByTextBinding.inflate(inflater)
        //        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.create_fruit_by_text_button)
            .setOnClickListener {
                viewModel.setTodayFruitList(
                    view.findViewById<EditText>(R.id.input_by_text_edit_text).text.toString(),
                )
            }
        viewLifecycleOwner.repeatOnStarted {
            viewModel.todayFruitList.collect {
                // TODO: 바깥 VIEWMODEL 임포트 -> ui state 변경 :: 근데 이거는 다음 프래그먼트에서 해도 되겠다.
                parentFragmentManager.beginTransaction()
                    .add(R.id.before_fragment_container_view, AfterFruitCreateFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
