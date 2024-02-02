package com.d101.frientree.dto.calendar.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CalendarWeeklyJuiceDTO {
    private CalendarWeeklyJuiceDetailDTO juiceData;
    private List<CalendarWeeklyJuiceFruitGraphDTO> fruitGraphData;

    public static CalendarWeeklyJuiceDTO createCalendarWeeklyJuiceDTO(
            CalendarWeeklyJuiceDetailDTO juiceData, List<CalendarWeeklyJuiceFruitGraphDTO> fruitGraphData) {
        return CalendarWeeklyJuiceDTO.builder()
                .juiceData(juiceData)
                .fruitGraphData(fruitGraphData)
                .build();
    }
}
