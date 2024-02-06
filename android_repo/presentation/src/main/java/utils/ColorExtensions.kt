package utils

import android.graphics.Color
import androidx.annotation.ColorInt
import kotlin.math.min

@ColorInt
fun Int.darkenColor(factor: Float = 1.1f): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(this, hsv)
    hsv[1] = min(1f, hsv[1] * factor) // 채도를 factor만큼 증가시킵니다. 최대 1
    hsv[2] *= 0.8f // 값(Value)를 줄여 색상을 더 진하게 합니다.
    return Color.HSVToColor(hsv)
}
