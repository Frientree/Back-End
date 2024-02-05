package com.d101.frientree.dto.juice.response.dto;

import com.d101.frientree.entity.fruit.UserFruit;
import lombok.Builder;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.*;

@Data
@Builder
public class JuiceFruitsGraphDataDTO {

    private String fruitDate;
    private String fruitCalendarImageUrl;
    private Long fruitScore;

    public static List<JuiceFruitsGraphDataDTO> createJuiceFruitsGraphDataDTO(Date startDate, Date endDate, List<UserFruit> userFruits) {
        Map<Date, JuiceFruitsGraphDataDTO> dateToDTO = new TreeMap<>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        while (!cal.getTime().after(endDate)) {
            dateToDTO.put(cal.getTime(), JuiceFruitsGraphDataDTO.builder()
                    .fruitDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()))
                    .fruitCalendarImageUrl("")
                    .fruitScore(11L)
                    .build());
            cal.add(Calendar.DATE, 1);
        }

        for (UserFruit fruit : userFruits) {
            JuiceFruitsGraphDataDTO dto = dateToDTO.get(fruit.getUserFruitCreateDate());
            if (dto != null) {
                dto.setFruitCalendarImageUrl(fruit.getFruitDetail().getFruitCalendarUrl());
                dto.setFruitScore(fruit.getUserFruitScore());
            }
        }

        return new ArrayList<>(dateToDTO.values());
    }
}
