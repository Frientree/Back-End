package com.d101.frientree.dto.calendar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarTodayFeelStatisticsDTO {
    @Schema(description = "금일 날짜", example = "2024-01-01")
    private String today;
    @Schema(description = "기분", example = "행복")
    private String feel;
    @Schema(description = "같은 사람 수", example = "5")
    private Long statistics;

    public static CalendarTodayFeelStatisticsDTO createCalendarTodayFeelStatisticsDTO(String today, String feel, Long statistics){
        return CalendarTodayFeelStatisticsDTO.builder()
                .today(today)
                .feel(feel)
                .statistics(statistics)
                .build();
    }
}
