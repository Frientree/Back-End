package com.d101.frientree.dto.calendar.response;

import com.d101.frientree.dto.calendar.dto.CalendarWeeklyFruitsDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CalendarWeeklyFruitsResponse {
    private String message;
    private List<CalendarWeeklyFruitsDTO> data;

    public static CalendarWeeklyFruitsResponse createCalendarWeeklyFruitsResponse(String message, List<CalendarWeeklyFruitsDTO> data){
        return CalendarWeeklyFruitsResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
