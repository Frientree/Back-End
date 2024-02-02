package com.d101.frientree.dto.calendar.response;

import com.d101.frientree.dto.calendar.dto.CalendarWeeklyJuiceDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarWeeklyJuiceResponse {
    private String message;
    private CalendarWeeklyJuiceDTO data;

    public CalendarWeeklyJuiceResponse createCalendarWeeklyJuiceResponse(String message, CalendarWeeklyJuiceDTO data){
        return CalendarWeeklyJuiceResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
