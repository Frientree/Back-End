package com.d101.frientree.dto.calendar.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarMonthlyFruitsDTO {
    private String day;
    public String fruitCalendarImageUrl;

    public static CalendarMonthlyFruitsDTO createCalendarMonthlyFruitsDTO(){
        return CalendarMonthlyFruitsDTO.builder().build();
    }
}
