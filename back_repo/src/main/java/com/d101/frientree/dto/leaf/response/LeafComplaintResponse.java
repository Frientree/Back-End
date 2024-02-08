package com.d101.frientree.dto.leaf.response;

import com.d101.frientree.dto.leaf.response.dto.LeafComplaintResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeafComplaintResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private Boolean data;
    public static LeafComplaintResponse createLeafComplaintResponse(String message, Boolean isComplained) {
        return LeafComplaintResponse.builder()
                .message(message)
                .data(isComplained)
                .build();
    }
}
