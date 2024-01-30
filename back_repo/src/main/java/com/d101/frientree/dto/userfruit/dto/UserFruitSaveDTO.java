package com.d101.frientree.dto.userfruit.dto;

import com.d101.frientree.entity.fruit.FruitDetail;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFruitSaveDTO {
    private Long fruitNum;
    private String fruitName;
    private String fruitDescription;
    private String fruitImageUrl;
    private String fruitFeel;

    public static UserFruitSaveDTO createUserFruitSaveDTO(FruitDetail fruitDetail){
        return UserFruitSaveDTO.builder()
                .fruitNum(fruitDetail.getFruitNum())
                .fruitName(fruitDetail.getFruitName())
                .fruitDescription(fruitDetail.getFruitDescription())
                .fruitImageUrl(fruitDetail.getFruitImageUrl())
                .fruitFeel(fruitDetail.getFruitFeel())
                .build();
    }
}
