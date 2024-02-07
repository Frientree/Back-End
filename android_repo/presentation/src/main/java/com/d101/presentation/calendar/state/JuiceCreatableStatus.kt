package com.d101.presentation.calendar.state

enum class JuiceCreatableStatus(val message: String) {
    NotEnoughFruits("주스를 만들기 위해선 주간 열매가 4개 이상 필요해요!"), JuiceCreatable(""), MoreTimeNeeded(
        "한주가 마무리 되어야 만들 수 있어요!",
    )
}
