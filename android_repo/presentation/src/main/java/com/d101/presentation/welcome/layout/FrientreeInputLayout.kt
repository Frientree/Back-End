package com.d101.presentation.welcome.layout

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.d101.presentation.R
import com.d101.presentation.databinding.LayoutInputTextInWelcomeBinding

class FrientreeInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : ConstraintLayout(context, attrs, defStyle) {

    private val binding: LayoutInputTextInWelcomeBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_input_text_in_welcome,
            this,
            true,
        )

    var label: String? = ""
    var text: String? = ""
    var hint: String? = ""
    var description: String? = ""
    var descriptionColor: Int = Color.BLACK
    var confirmButtonVisible: Boolean = true

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.FrientreeInputLayout,
            defStyle,
            0,
        )
        try {
            label = typedArray.getString(R.styleable.FrientreeInputLayout_label)
            text = typedArray.getString(R.styleable.FrientreeInputLayout_text)
            hint = typedArray.getString(R.styleable.FrientreeInputLayout_hint)
            description = typedArray.getString(R.styleable.FrientreeInputLayout_description)
            descriptionColor =
                typedArray.getColor(R.styleable.FrientreeInputLayout_descriptionColor, Color.BLACK)
            confirmButtonVisible =
                typedArray.getBoolean(R.styleable.FrientreeInputLayout_confirmButtonVisible, false)
        } finally {
            typedArray.recycle()
        }

        updateViews()
    }

    private fun updateViews() {
        binding.labelTextView.text = label
        binding.inputEditText.setText(text)
        binding.inputEditText.hint = hint
        binding.descriptionTextView.text = description
        binding.confirmButton.visibility =
            if (confirmButtonVisible) View.VISIBLE else View.INVISIBLE
    }
}
