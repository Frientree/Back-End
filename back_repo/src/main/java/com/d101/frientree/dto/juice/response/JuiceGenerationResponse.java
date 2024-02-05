package com.d101.frientree.dto.juice.response;

import com.d101.frientree.dto.juice.response.dto.JuiceDataDTO;
import com.d101.frientree.dto.juice.response.dto.JuiceFruitsGraphDataDTO;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class JuiceGenerationResponse {

    private String message;
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
