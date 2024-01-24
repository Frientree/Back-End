package com.d101.data.model.calendar.response

data class WeeklyJuiceResponse(
    val juiceData: JuiceDataResponse,
    val fruitsGraphData: List<FruitGraphElementResponse>,
)
