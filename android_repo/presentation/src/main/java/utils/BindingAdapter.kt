package utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, imageUrl: String?) {
        if (imageUrl != null) {
            Glide.with(view.context).load(imageUrl)
                .apply(RequestOptions().override(Target.SIZE_ORIGINAL)).into(view)
        }
    }
}
