package com.d101.frientree.dto.calendar.dto;

import com.d101.frientree.entity.fruit.UserFruit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarWeeklyJuiceFruitsGraphDTO {
    @Schema(description = "열매 생성 날짜", example = "2024-01-01")
    private String fruitDate;
    @Schema(description = "열매 캘린더 이미지 URL", example = "www.aaa.com")
    private String fruitCalendarImageUrl;
    @Schema(description = "열매 점수", example = "19")
    private Long fruitScore;

    public static CalendarWeeklyJuiceFruitsGraphDTO createCalendarWeeklyJuiceFruitGraphDTO(String fruitDate,
                                                                                           UserFruit userFruit){
        return CalendarWeeklyJuiceFruitsGraphDTO.builder()
                .fruitDate(fruitDate)
                .fruitCalendarImageUrl(userFruit.getFruitDetail().getFruitCalendarUrl())
                .fruitScore(userFruit.getUserFruitScore())
                .build();
    }
    public static CalendarWeeklyJuiceFruitsGraphDTO createCalendarWeeklyJuiceFruitGraphDTO(String fruitDate){
        return CalendarWeeklyJuiceFruitsGraphDTO.builder()
                .fruitDate(fruitDate)
                .fruitCalendarImageUrl("")
                .fruitScore(11L)
                .build();
    }
}
