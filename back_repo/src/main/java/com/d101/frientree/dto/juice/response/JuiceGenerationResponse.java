package com.d101.frientree.dto.juice.response;

import com.d101.frientree.dto.juice.response.dto.JuiceGenerationResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JuiceGenerationResponse {

    private String message;
    private JuiceGenerationResponseDTO dto;

    public static JuiceGenerationResponse createJuiceGenerationResponse(String message, JuiceGenerationResponseDTO dto) {
        return JuiceGenerationResponse.builder()
                .message(message)
                .dto(dto)
                .build();
    }
}
