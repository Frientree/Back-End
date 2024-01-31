package utils

import android.widget.Button
import androidx.databinding.BindingAdapter
import com.d101.presentation.R
import com.d101.presentation.welcome.layout.FrientreeInputLayout

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("bind:onConfirmClick")
    fun setOnConfirmClickListener(
        view: FrientreeInputLayout,
        listener: FrientreeInputLayout.OnConfirmClickListener,
    ) {
        view.findViewById<Button>(R.id.confirm_button).setOnClickListener {
            listener.setOnClickConfirm()
        }
    }
}
