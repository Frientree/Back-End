package com.d101.frientree.dto.userfruit.dto;

import com.d101.frientree.entity.fruit.FruitDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFruitCreateDTO {
    @Schema(description = "열매 PK 번호", nullable = false, example = "2")
    private Long fruitNum;
    @Schema(description = "열매 이름", nullable = false, example = "딸기")
    private String fruitName;
    @Schema(description = "열매 세부 내용", nullable = false, example = "딸기는 맛있어요~!~!")
    private String fruitDescription;
    @Schema(description = "열매 기본 이미지 URL", nullable = false, example = "www.abcd.com")
    private String fruitImageUrl;
    @Schema(description = "기분", nullable = false, example = "행복")
    private String fruitFeel;

    public static UserFruitCreateDTO createUserFruitCreateDTO(FruitDetail fruitDetail){
        return UserFruitCreateDTO.builder()
                .fruitNum(fruitDetail.getFruitNum())
                .fruitName(fruitDetail.getFruitName())
                .fruitDescription(fruitDetail.getFruitDescription())
                .fruitImageUrl(fruitDetail.getFruitImageUrl())
                .fruitFeel(fruitDetail.getFruitFeel())
                .build();
    }
}
