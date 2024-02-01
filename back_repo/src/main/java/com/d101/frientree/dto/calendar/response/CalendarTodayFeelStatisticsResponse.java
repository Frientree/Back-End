package com.d101.frientree.dto.calendar.response;

import com.d101.frientree.dto.calendar.dto.CalendarTodayFeelStatisticsDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarTodayFeelStatisticsResponse {
    private String message;
    private CalendarTodayFeelStatisticsDTO data;

    public static CalendarTodayFeelStatisticsResponse createCalendarTodayFeelStatisticsResponse(String message, CalendarTodayFeelStatisticsDTO data){
        return CalendarTodayFeelStatisticsResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
