package com.d101.frientree.dto.juice.response;

import com.d101.frientree.dto.juice.response.dto.JuiceConfirmationResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JuiceConfirmationResponse {

    private String message;
    private JuiceConfirmationResponseDTO dto;

    public static JuiceConfirmationResponse createJuiceConfirmationResponse(String message, JuiceConfirmationResponseDTO dto) {
        return JuiceConfirmationResponse.builder()
                .message(message)
                .dto(dto)
                .build();
    }
}
