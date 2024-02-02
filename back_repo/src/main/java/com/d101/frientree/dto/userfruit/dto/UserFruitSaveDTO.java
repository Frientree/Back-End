package com.d101.frientree.dto.userfruit.dto;

import com.d101.frientree.entity.fruit.FruitDetail;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFruitSaveDTO {
    private Boolean isApple;
    private String fruitCreateDare;
    private String fruitName;
    private String fruitDescription;
    private String fruitImageUrl;
    private String fruitCalendarImageUrl;
    private String fruitFeel;
    private Long fruitScore;

    public static UserFruitSaveDTO createUserFruitSaveDTO(Boolean isApple, String fruitCreateDare,FruitDetail fruitDetail, Long score){
        return UserFruitSaveDTO.builder()
                .isApple(isApple)
                .fruitCreateDare(fruitCreateDare)
                .fruitName(fruitDetail.getFruitName())
                .fruitDescription(fruitDetail.getFruitDescription())
                .fruitImageUrl(fruitDetail.getFruitImageUrl())
                .fruitCalendarImageUrl(fruitDetail.getFruitCalendarUrl())
                .fruitFeel(fruitDetail.getFruitFeel())
                .fruitScore(score)
                .build();
    }
}
