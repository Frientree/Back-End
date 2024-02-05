package com.d101.frientree.dto.calendar.dto;

import com.d101.frientree.entity.fruit.FruitDetail;
import com.d101.frientree.entity.fruit.UserFruit;
import lombok.Builder;
import lombok.Data;

import java.util.Locale;

@Data
@Builder
public class CalendarWeeklyFruitsDTO {
    private String fruitDay;
    private String fruitName;
    private String fruitFeel;
    private String fruitDescription;
    private String fruitImageUrl;
    private String fruitCalendarImageUrl;

    public static CalendarWeeklyFruitsDTO createCalendarWeeklyFruitsDTO(String fruitDay, UserFruit userFruit){
        return CalendarWeeklyFruitsDTO.builder()
                .fruitDay(fruitDay)
                .fruitName(userFruit.getFruitDetail().getFruitName())
                .fruitFeel(userFruit.getFruitDetail().getFruitFeel())
                .fruitDescription(userFruit.getFruitDetail().getFruitDescription())
                .fruitImageUrl(userFruit.getFruitDetail().getFruitImageUrl())
                .fruitCalendarImageUrl(userFruit.getFruitDetail().getFruitCalendarUrl())
                .build();
    }
}
