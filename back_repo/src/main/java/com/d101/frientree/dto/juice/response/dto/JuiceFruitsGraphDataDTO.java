package com.d101.frientree.dto.juice.response.dto;

import com.d101.frientree.entity.fruit.UserFruit;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class JuiceFruitsGraphDataDTO {

    private String fruitDate;
    private String fruitCalendarImageUrl;
    private Long fruitScore;

    public static List<JuiceFruitsGraphDataDTO> createJuiceFruitsGraphDataDTO(List<UserFruit> userFruits) {
        List<JuiceFruitsGraphDataDTO> result = new ArrayList<>();

        for (UserFruit userFruit : userFruits) {
            JuiceFruitsGraphDataDTO dto = JuiceFruitsGraphDataDTO.builder()
                    .fruitDate(userFruit.getUserFruitCreateDate().toString())
                    .fruitCalendarImageUrl(userFruit.getFruitDetail().getFruitCalendarUrl())
                    .fruitScore(userFruit.getUserFruitScore())
                    .build();

            result.add(dto);
        }

        return result;
    }
}
