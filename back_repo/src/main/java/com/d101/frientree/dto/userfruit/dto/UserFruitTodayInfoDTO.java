package com.d101.frientree.dto.userfruit.dto;

import com.d101.frientree.entity.fruit.FruitDetail;
import com.d101.frientree.entity.fruit.UserFruit;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFruitTodayInfoDTO {
    private String fruitCreateDate;
    private String fruitName;
    private String fruitDescription;
    private String fruitImageUrl;
    private String fruitCalendarImageUrl;
    private String fruitFeel;
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
