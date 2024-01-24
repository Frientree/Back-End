package com.d101.data.model.juice.response

data class JuiceCreateResponse(
    val juiceData: JuiceDataModel,
    val fruitsGraphData: List<FruitGraphElementModel>,
)
