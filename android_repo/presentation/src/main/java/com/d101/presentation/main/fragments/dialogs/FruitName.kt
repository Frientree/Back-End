package com.d101.presentation.main.fragments.dialogs

enum class FruitName(val feelEng: String, val feelKor: String) {

    LUCK("luck", "행운"),
    HAPPY("happy", "행복"),
    EXCITING("exciting", "신남"),
    SOSO("soso", "쏘쏘"),
    SAD("sad", "우울"),
    ANNOYING("annoying", "짜증"),
    TIRED("tired", "피곤"),
    WORRIED("worried", "걱정");

    companion object {
        fun getFeelKor(feelEng: String): String {
            return values().find {
                it.feelEng.equals(feelEng, ignoreCase = true)
            }?.feelKor ?: "알 수 없는 감정"
        }
    }
}
