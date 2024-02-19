package com.d101.frientree.dto.juice.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JuiceGenerationRequest {
    @Schema(description = "시작날짜", example = "2024-01-28")
    private String startDate;
    @Schema(description = "마지막날짜", example = "2024-02-03")
    private String endDate;
}
