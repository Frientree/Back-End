package com.d101.frientree.dto.calendar.response;

import com.d101.frientree.dto.calendar.dto.CalendarMonthlyFruitsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CalendarMonthlyFruitsResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    public List<CalendarMonthlyFruitsDTO> data;

    public static CalendarMonthlyFruitsResponse createCalendarMonthlyFruitsResponse(String message, List<CalendarMonthlyFruitsDTO> data){
        return CalendarMonthlyFruitsResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
