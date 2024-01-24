package com.d101.data.model.juice.response

import com.d101.data.model.juice.FruitGraphElementModel
import com.d101.data.model.juice.JuiceDataModel

data class JuiceCreationResponse(
    val juiceData: JuiceDataModel,
    val fruitsGraphData: List<FruitGraphElementModel>,
)
