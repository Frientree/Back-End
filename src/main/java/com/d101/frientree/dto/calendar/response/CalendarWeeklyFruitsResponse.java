package com.d101.frientree.dto.calendar.response;

import com.d101.frientree.dto.calendar.dto.CalendarWeeklyFruitsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CalendarWeeklyFruitsResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private List<CalendarWeeklyFruitsDTO> data;

    public static CalendarWeeklyFruitsResponse createCalendarWeeklyFruitsResponse(String message, List<CalendarWeeklyFruitsDTO> data){
        return CalendarWeeklyFruitsResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
