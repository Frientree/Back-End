package com.d101.frientree.dto.leaf.response;

import com.d101.frientree.dto.leaf.response.dto.LeafGenerationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LeafGenerationResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private Boolean data;

    public static LeafGenerationResponse createLeafGenerationResponse(String message, Boolean isCreated) {
        return LeafGenerationResponse.builder()
                .message(message)
                .data(isCreated)
                .build();
    }
}
