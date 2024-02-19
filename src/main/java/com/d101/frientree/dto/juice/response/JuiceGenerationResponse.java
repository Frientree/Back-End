package com.d101.frientree.dto.juice.response;

import com.d101.frientree.dto.juice.response.dto.JuiceDataDTO;
import com.d101.frientree.dto.juice.response.dto.JuiceFruitsGraphDataDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class JuiceGenerationResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private Map<String, Object> data;

    public static JuiceGenerationResponse createJuiceGenerationResponse(String message, JuiceDataDTO juiceData, List<JuiceFruitsGraphDataDTO> fruitsGraphData) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("juiceData", juiceData);
        dataMap.put("fruitsGraphData", fruitsGraphData);

        return JuiceGenerationResponse.builder()
                .message(message)
                .data(dataMap)
                .build();
    }
}
