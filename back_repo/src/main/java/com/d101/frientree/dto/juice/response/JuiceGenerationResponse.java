package com.d101.frientree.dto.juice.response;

import com.d101.frientree.dto.juice.response.dto.JuiceDataDTO;
import com.d101.frientree.dto.juice.response.dto.JuiceFruitsGraphDataDTO;
import com.d101.frientree.dto.juice.response.dto.JuiceGenerationResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JuiceGenerationResponse {

    private String message;
    private List<JuiceFruitsGraphDataDTO> fruitsGraphData;
    private JuiceDataDTO juiceData;

    public static JuiceGenerationResponse createJuiceGenerationResponse(String message, JuiceDataDTO juiceData, List<JuiceFruitsGraphDataDTO> fruitsGraphData) {
        return JuiceGenerationResponse.builder()
                .message(message)
                .juiceData(juiceData)
                .fruitsGraphData(fruitsGraphData)
                .build();
    }
}
