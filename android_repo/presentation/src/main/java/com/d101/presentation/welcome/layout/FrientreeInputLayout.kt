package com.d101.presentation.welcome.layout

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.d101.presentation.R
import com.d101.presentation.databinding.LayoutInputTextInWelcomeBinding
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

    var label: Int = R.string.empty_text
        set(value) {
            field = value
            binding.labelTextView.text = context.getText(value)
        }

    fun bindTextFlow(lifecycleOwner: LifecycleOwner, textFlow: MutableStateFlow<String>) {
        lifecycleOwner.lifecycleScope.launch {
            textFlow.collect { newValue ->
                binding.inputEditText.let {
                    if (it.text.toString() != newValue) {
                        it.setText(newValue)
                        it.setSelection(newValue.length)
                    }
                }
            }
        }
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != textFlow.value) {
                    textFlow.value = s.toString()
                    setEditTextInputType()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    var hint: Int = R.string.empty_text
        set(value) {
            field = value
            binding.inputEditText.hint = context.getText(value)
        }
    var description: Int = R.string.empty_text
        set(value) {
            field = value
            binding.descriptionTextView.text = context.getString(value)
        }

    @ColorRes
    var descriptionColor: Int = R.color.black
        set(value) {
            field = value
            binding.descriptionTextView.setTextColor(ContextCompat.getColor(context, value))
        }
    var confirmButtonVisible: Boolean = true
        set(value) {
            field = value
            binding.confirmButton.visibility =
                if (value) View.VISIBLE else View.INVISIBLE
        }

    var isPasswordInputType: Boolean = false
        set(value) {
            field = value
            setEditTextInputType()
        }

    private fun setEditTextInputType() {
        binding.inputEditText.inputType =
            if (isPasswordInputType) {
                binding.inputEditText.transformationMethod = PasswordTransformationMethod()
                InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT
            }
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.FrientreeInputLayout,
            defStyle,
            0,
        )
        try {
            label = typedArray.getInt(
                R.styleable.FrientreeInputLayout_description,
                R.string.empty_text,
            )
            hint = typedArray.getInt(
                R.styleable.FrientreeInputLayout_description,
                R.string.empty_text,
            )
            description = typedArray.getInt(
                R.styleable.FrientreeInputLayout_description,
                R.string.usable_id,
            )
            descriptionColor = typedArray.getInt(
                R.styleable.FrientreeInputLayout_descriptionColor,
                R.color.black,
            )
            confirmButtonVisible = typedArray.getBoolean(
                R.styleable.FrientreeInputLayout_confirmButtonVisible,
                false,
            )
        } finally {
            typedArray.recycle()
        }
    }
}
