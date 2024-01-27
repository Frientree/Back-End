package com.d101.presentation.main.fragments.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.d101.presentation.databinding.FragmentAfterFruitCreateBaseBinding

class AfterFruitCreateBaseFragment : DialogFragment() {

    private var _binding: FragmentAfterFruitCreateBaseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAfterFruitCreateBaseBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        childFragmentManager.beginTransaction()
//            .add(R.id.fragment_container_view, SelectCreateFruitMethodFragment())
//            .addToBackStack(null)
//            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}
