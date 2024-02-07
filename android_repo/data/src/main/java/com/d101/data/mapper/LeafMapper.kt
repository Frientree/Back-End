package com.d101.data.mapper

import com.d101.data.model.leaf.response.LeafResponse
import com.d101.domain.model.Leaf

object LeafMapper {
    fun LeafResponse.toLeaf(): Leaf {
        return Leaf(
            this.leafNum,
            this.leafContent,
        )
    }
}
