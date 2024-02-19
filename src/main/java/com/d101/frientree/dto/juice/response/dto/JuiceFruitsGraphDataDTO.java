package com.d101.frientree.dto.juice.response.dto;

import com.d101.frientree.entity.fruit.UserFruit;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "열매 생성 날짜", example = "2024-01-01")
    private String fruitDate;
    @Schema(description = "열매 캘린더 이미지 URL", example = "www.efgh.com")
    private String fruitCalendarImageUrl;
    @Schema(description = "열매 점수", example = "12")
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
