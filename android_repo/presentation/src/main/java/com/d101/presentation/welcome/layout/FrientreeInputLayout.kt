package com.d101.presentation.welcome.layout

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.d101.presentation.R
import com.d101.presentation.databinding.LayoutInputTextInWelcomeBinding
import com.d101.presentation.welcome.state.InputDataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FrientreeInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : ConstraintLayout(context, attrs, defStyle) {

    interface OnConfirmClickListener {
        fun setOnClickConfirm()
    }

    private val binding: LayoutInputTextInWelcomeBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_input_text_in_welcome,
            this,
            true,
        )

    private var textInputType = InputType.TYPE_CLASS_TEXT
        set(value) {
            field = value
            binding.inputEditText.inputType = value
        }

    fun bindTextFlow(lifecycleOwner: LifecycleOwner, textFlow: MutableStateFlow<String>) {
        lifecycleOwner.lifecycleScope.launch {
            textFlow.collect { newValue ->
                binding.inputEditText.let {
                    if (it.text.toString() != newValue) {
                        it.setText(newValue)
                    }
                }
            }
        }
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != textFlow.value) {
                    textFlow.value = s.toString()
                    if (textInputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                        binding.inputEditText.apply {
                            inputType = textInputType
                            transformationMethod = PasswordTransformationMethod.getInstance()
                            PasswordTransformationMethod()
                            setSelection(text.length)
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun setInputDataState(inputDataState: InputDataState) {
        binding.labelTextView.text = context.getText(inputDataState.label)
        binding.inputEditText.hint = context.getText(inputDataState.hint)
        binding.inputEditText.isEnabled = inputDataState.inputEnabled
        binding.confirmButton.visibility =
            if (inputDataState.buttonVisible) View.VISIBLE else View.INVISIBLE
        binding.confirmButton.isEnabled = inputDataState.buttonEnabled
        binding.confirmButton.text = context.getText(inputDataState.buttonType.stringRes)
        binding.descriptionTextView.setText(inputDataState.description)
        binding.descriptionTextView.setTextColor(
            ContextCompat.getColor(
                context,
                inputDataState.descriptionType.colorRes,
            ),
        )
        textInputType = inputDataState.inputType
    }
}
