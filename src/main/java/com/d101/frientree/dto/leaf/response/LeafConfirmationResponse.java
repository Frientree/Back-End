package com.d101.frientree.dto.leaf.response;

import com.d101.frientree.dto.leaf.response.dto.LeafConfirmationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeafConfirmationResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private LeafConfirmationResponseDTO data;

    public static LeafConfirmationResponse createLeafConfirmationResponse(String message, LeafConfirmationResponseDTO dto) {
        return com.d101.frientree.dto.leaf.response.LeafConfirmationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
