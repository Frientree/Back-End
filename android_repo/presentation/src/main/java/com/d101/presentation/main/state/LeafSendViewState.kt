package com.d101.presentation.main.state

sealed class LeafSendViewState {
    abstract val leafSendTitle: String
    data class NoSendLeafSendViewState(
        override val leafSendTitle: String = "다른 사람들에게 힘이 되는 이파리를 날려보세요!",
    ) : LeafSendViewState()

    data class ZeroViewLeafSendViewState(
        override val leafSendTitle: String = "당신의 이파리가 날아다니는 중이에요!",
    ) : LeafSendViewState()

    data class SomeViewLeafSateSendView(
        override val leafSendTitle: String = "",
    ) : LeafSendViewState()
}
