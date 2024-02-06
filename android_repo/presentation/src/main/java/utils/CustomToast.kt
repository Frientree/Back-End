package utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.d101.presentation.databinding.ToastCustomBinding

object CustomToast {
    fun createAndShow(context: Context, message: String) {
        val binding: ToastCustomBinding =
            ToastCustomBinding.inflate(LayoutInflater.from(context), null, false)

        binding.message.text = message

        return Toast(context).apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 80)
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }.show()
    }
}
