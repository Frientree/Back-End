package com.d101.frientree.dto.calendar.response;

import com.d101.frientree.dto.calendar.dto.CalendarWeeklyJuiceDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarWeeklyJuiceResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private CalendarWeeklyJuiceDTO data;

    public static CalendarWeeklyJuiceResponse createCalendarWeeklyJuiceResponse(String message, CalendarWeeklyJuiceDTO data){
        return CalendarWeeklyJuiceResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}

