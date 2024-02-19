package com.d101.frientree.dto.calendar.response;

import com.d101.frientree.dto.calendar.dto.CalendarTodayFeelStatisticsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarTodayFeelStatisticsResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private CalendarTodayFeelStatisticsDTO data;

    public static CalendarTodayFeelStatisticsResponse createCalendarTodayFeelStatisticsResponse(String message, CalendarTodayFeelStatisticsDTO data){
        return CalendarTodayFeelStatisticsResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
