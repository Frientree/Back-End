package com.d101.frientree.dto.juice.response;

import com.d101.frientree.dto.juice.response.dto.JuiceListConfirmationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JuiceListConfirmationResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private List<JuiceListConfirmationResponseDTO> data;

    public static JuiceListConfirmationResponse createJuiceListConfirmationResponse(String message, List<JuiceListConfirmationResponseDTO> dto) {

        return JuiceListConfirmationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
