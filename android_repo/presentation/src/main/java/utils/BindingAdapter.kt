package utils

import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import com.d101.presentation.R
import com.d101.presentation.welcome.layout.FrientreeInputLayout
import kotlinx.coroutines.flow.MutableStateFlow

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

    @JvmStatic
    @BindingAdapter("bind:lifecycleOwner", "bind:text")
    fun setText(
        view: FrientreeInputLayout,
        lifecycleOwner: LifecycleOwner,
        textFlow: MutableStateFlow<String>,
    ) {
        view.bindTextFlow(lifecycleOwner, textFlow)
    }
}
