package com.d101.frientree.dto.calendar.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarWeeklyJuiceFruitGraphDTO {
    private String fruitDate;
    private String fruitCalendarImageUrl;
    private Long fruitScore;

    public static CalendarWeeklyJuiceFruitGraphDTO createCalendarWeeklyJuiceFruitGraphDTO(){
        return CalendarWeeklyJuiceFruitGraphDTO.builder().build();
    }
}
