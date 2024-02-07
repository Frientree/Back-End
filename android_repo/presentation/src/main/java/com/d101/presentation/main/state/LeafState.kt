package com.d101.presentation.main.state

sealed class LeafState {
    abstract val leafSendTitle: String
    data class NoSendLeafState(
        override val leafSendTitle: String = "다른 사람들에게 힘이 되는 이파리를 날려보세요!",
    ) : LeafState()

    data class ZeroViewLeafState(
        override val leafSendTitle: String = "당신의 이파리가 날아다니는 중이에요!",
    ) : LeafState()

    data class SomeViewLeafSate(
        override val leafSendTitle: String = "",
    ) : LeafState()
}
