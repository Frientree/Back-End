package com.d101.frientree.dto.calendar.dto;

import com.d101.frientree.entity.fruit.UserFruit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarMonthlyFruitsDTO {
    @Schema(description = "일자", example = "2024-01-01")
    private String day;
    @Schema(description = "캘린더 열매 이미지 URL", nullable = true, example = "www.abcd.com")
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
