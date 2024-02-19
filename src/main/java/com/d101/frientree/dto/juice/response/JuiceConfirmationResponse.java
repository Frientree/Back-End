package com.d101.frientree.dto.juice.response;

import com.d101.frientree.dto.juice.response.dto.JuiceConfirmationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JuiceConfirmationResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private JuiceConfirmationResponseDTO dto;

    public static JuiceConfirmationResponse createJuiceConfirmationResponse(String message, JuiceConfirmationResponseDTO dto) {
        return JuiceConfirmationResponse.builder()
                .message(message)
                .dto(dto)
                .build();
    }
}
