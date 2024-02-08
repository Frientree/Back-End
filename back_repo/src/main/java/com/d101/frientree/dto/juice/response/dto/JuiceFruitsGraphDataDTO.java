package com.d101.frientree.dto.juice.response.dto;

import com.d101.frientree.entity.fruit.UserFruit;
import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Data
@Builder
public class JuiceFruitsGraphDataDTO {

    private String fruitDate;
    private String fruitCalendarImageUrl;
    private Long fruitScore;

    public static List<JuiceFruitsGraphDataDTO> createJuiceFruitsGraphDataDTO(LocalDate startDate, LocalDate endDate, List<UserFruit> userFruits) {
        Map<LocalDate, JuiceFruitsGraphDataDTO> dateToDTO = new TreeMap<>();

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            dateToDTO.put(currentDate, JuiceFruitsGraphDataDTO.builder()
                    .fruitDate(currentDate.toString())
                    .fruitCalendarImageUrl("")
                    .fruitScore(11L)
                    .build());
            currentDate = currentDate.plusDays(1);
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
