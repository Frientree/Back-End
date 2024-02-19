package com.d101.frientree.dto.userfruit.dto;

import com.d101.frientree.entity.fruit.UserFruit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFruitTodayInfoDTO {
    @Schema(description = "열매 생성 날짜", example = "2024-01-01")
    private String fruitCreateDate;
    @Schema(description = "열매 이름", example = "딸기")
    private String fruitName;
    @Schema(description = "열매 세부 내용", example = "딸기는 기분이 좋아지는 열매입니다.")
    private String fruitDescription;
    @Schema(description = "열매 기본 이미지 URL", example = "www.abcd.com")
    private String fruitImageUrl;
    @Schema(description = "열매 캘린더 이미지 URL", example = "www.efgh.com")
    private String fruitCalendarImageUrl;
    @Schema(description = "기분", example = "행복")
    private String fruitFeel;
    @Schema(description = "열매 점수", example = "21")
    private Long fruitScore;

    public static UserFruitTodayInfoDTO createUserFruitTodayInfoDTO(UserFruit userFruit){
        return UserFruitTodayInfoDTO.builder()
                .fruitCreateDate(userFruit.getUserFruitCreateDate().toString())
                .fruitName(userFruit.getFruitDetail().getFruitName())
                .fruitDescription(userFruit.getFruitDetail().getFruitDescription())
                .fruitImageUrl(userFruit.getFruitDetail().getFruitImageUrl())
                .fruitCalendarImageUrl(userFruit.getFruitDetail().getFruitCalendarUrl())
                .fruitFeel(userFruit.getFruitDetail().getFruitFeel())
                .fruitScore(userFruit.getUserFruitScore())
                .build();
    }
}
