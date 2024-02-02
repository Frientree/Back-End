package com.d101.frientree.dto.calendar.dto;

import com.d101.frientree.entity.fruit.UserFruit;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarWeeklyJuiceFruitsGraphDTO {
    private String fruitDate;
    private String fruitCalendarImageUrl;
    private Long fruitScore;

    public static CalendarWeeklyJuiceFruitsGraphDTO createCalendarWeeklyJuiceFruitGraphDTO(String fruitDate, UserFruit userFruit){
        return CalendarWeeklyJuiceFruitsGraphDTO.builder()
                .fruitDate(fruitDate)
                .fruitCalendarImageUrl(userFruit.getFruitDetail().getFruitCalendarUrl())
                .fruitScore(userFruit.getUserFruitScore())
                .build();
    }
}
