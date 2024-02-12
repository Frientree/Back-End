package com.d101.frientree.dto.leaf.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LeafGenerationResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private Integer data;

    public static LeafGenerationResponse createLeafGenerationResponse(String message, Integer leafCount) {
        return LeafGenerationResponse.builder()
                .message(message)
                .data(leafCount)
                .build();
    }
}
