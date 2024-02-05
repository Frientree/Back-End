package com.d101.frientree.dto.calendar.dto;

import com.d101.frientree.entity.fruit.UserFruit;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarMonthlyFruitsDTO {
    private String day;
    public String fruitCalendarImageUrl;

    public static CalendarMonthlyFruitsDTO createCalendarMonthlyFruitsDTO(String day, UserFruit userFruit){
        return CalendarMonthlyFruitsDTO.builder()
                .day(day)
                .fruitCalendarImageUrl(userFruit.getFruitDetail().getFruitCalendarUrl())
                .build();
    }

    public static CalendarMonthlyFruitsDTO createCalendarMonthlyFruitsDTO(String day){
        return CalendarMonthlyFruitsDTO.builder()
                .day(day)
                .fruitCalendarImageUrl("")
                .build();
    }
}
