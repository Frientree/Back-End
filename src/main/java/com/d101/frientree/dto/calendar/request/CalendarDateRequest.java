package com.d101.frientree.dto.calendar.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CalendarDateRequest {
    @Schema(description = "START DATE", example = "2024-01-01")
    private String startDate;
    @Schema(description = "END DATE", example = "2024-01-07")
    public String endDate;
}
