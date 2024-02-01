package com.d101.frientree.dto.calendar.response;

import com.d101.frientree.dto.calendar.dto.CalendarMonthlyFruitsDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CalendarMonthlyFruitsResponse {
    private String message;
    public List<CalendarMonthlyFruitsDTO> data;

    public static CalendarMonthlyFruitsResponse createCalendarMonthlyFruitsResponse(String message, List<CalendarMonthlyFruitsDTO> data){
        return CalendarMonthlyFruitsResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
