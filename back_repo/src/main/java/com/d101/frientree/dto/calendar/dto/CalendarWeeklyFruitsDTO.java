package com.d101.frientree.dto.calendar.dto;

import com.d101.frientree.entity.fruit.FruitDetail;
import com.d101.frientree.entity.fruit.UserFruit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Locale;

@Data
@Builder
public class CalendarWeeklyFruitsDTO {
    @Schema(description = "열매 생성 날짜", example = "2024-01-01")
    private String fruitDay;
    @Schema(description = "열매 이름", example = "딸기")
    private String fruitName;
    @Schema(description = "기분", example = "행복")
    private String fruitFeel;
    @Schema(description = "열매 세부 내용", example = "딸기는 기분이 좋아지는 열매지롱~~")
    private String fruitDescription;
    @Schema(description = "열매 기본 이미지 URL", example = "www.abcd.com")
    private String fruitImageUrl;
    @Schema(description = "열매 캘린더 이미지 URL", example = "www.efgh.com")
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
