package com.d101.domain.utils

import java.util.Locale

enum class FruitEmotion(val english: String, val korean: String) {
    LUCK("luck", "행운"),
    HAPPY("happy", "행복"),
    EXCITING("exciting", "신남"),
    SOSO("soso", "무난"),
    SAD("sad", "우울"),
    ANNOYING("annoying", "짜증"),
    TIRED("tired", "피곤"),
    WORRIED("worried", "걱정"),
    UNKNOWN("unknown", "모름"),
}

fun String.toFruitEmotion(): FruitEmotion {
    return when (this.lowercase(Locale.ENGLISH)) {
        "luck" -> return FruitEmotion.LUCK
        "happy" -> return FruitEmotion.HAPPY
        "exciting" -> return FruitEmotion.EXCITING
        "soso" -> return FruitEmotion.SOSO
        "sad" -> return FruitEmotion.SAD
        "annoying" -> return FruitEmotion.ANNOYING
        "tired" -> return FruitEmotion.TIRED
        "worried" -> return FruitEmotion.WORRIED
        else -> FruitEmotion.UNKNOWN
    }
}
