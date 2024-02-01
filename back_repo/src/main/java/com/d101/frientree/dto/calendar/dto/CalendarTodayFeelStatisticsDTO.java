package com.d101.frientree.dto.calendar.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarTodayFeelStatisticsDTO {
    private String today;
    private String feel;
    private Long statistics;

    public static CalendarTodayFeelStatisticsDTO createCalendarTodayFeelStatisticsDTO(String today, String feel, Long statistics){
        return CalendarTodayFeelStatisticsDTO.builder()
                .today(today)
                .feel(feel)
                .statistics(statistics)
                .build();
    }
}
