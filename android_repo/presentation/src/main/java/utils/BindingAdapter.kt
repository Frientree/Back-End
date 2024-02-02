package utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.d101.presentation.welcome.layout.FrientreeInputLayout
import kotlinx.coroutines.flow.MutableStateFlow

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, imageUrl: String?) {
        if (imageUrl != null) {
            Glide.with(view.context).load(imageUrl)
                .apply(RequestOptions().override(Target.SIZE_ORIGINAL)).into(view)
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
