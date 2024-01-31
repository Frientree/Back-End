package com.d101.presentation.welcome.layout

import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.d101.presentation.R
import com.d101.presentation.databinding.LayoutInputTextInWelcomeBinding

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
    var text: Int = R.string.empty_text
        set(value) {
            field = value
            binding.inputEditText.setText(value)
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
            binding.inputEditText.inputType =
                if (value) {
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
            text = typedArray.getInt(
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
