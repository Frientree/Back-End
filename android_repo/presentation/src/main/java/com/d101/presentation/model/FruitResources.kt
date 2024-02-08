package com.d101.domain.model

import androidx.annotation.ColorRes
import com.d101.domain.utils.FruitEmotion
import com.d101.presentation.R

enum class FruitResources(
    val fruitEmotion: FruitEmotion,
    @ColorRes val color: Int,
    val fallingImage: Int,
) {
    APPLE(FruitEmotion.LUCK, R.color.fruit_purple_background, R.raw.falling_little_apples),
    STRAWBERRY(FruitEmotion.HAPPY, R.color.fruit_red_background, R.raw.falling_litte_strawberries),
    LEMON(FruitEmotion.EXCITING, R.color.fruit_yellow_background, R.raw.falling_little_lemons),
    MANGO(FruitEmotion.SOSO, R.color.fruit_salgu_background, R.raw.falling_little_mangos),
    KIWI(FruitEmotion.WORRIED, R.color.fruit_green_background, R.raw.falling_little_kiwis),
    CHERRY(FruitEmotion.TIRED, R.color.fruit_cherry_background, R.raw.falling_little_cherries),
    BLUEBERRY(FruitEmotion.SAD, R.color.fruit_blue_background, R.raw.falling_little_blueberries),
    GRAPE(FruitEmotion.ANNOYING, R.color.fruit_navy_background, R.raw.falling_little_grapes),
}
