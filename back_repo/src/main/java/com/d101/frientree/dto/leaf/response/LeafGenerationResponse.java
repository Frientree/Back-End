package com.d101.frientree.dto.leaf.response;

import com.d101.frientree.dto.leaf.response.dto.LeafGenerationResponseDTO;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LeafGenerationResponse {


    private String message;
    private Boolean data;

    public static LeafGenerationResponse createLeafGenerationResponse(String message, Boolean isCreated) {
        return LeafGenerationResponse.builder()
                .message(message)
                .data(isCreated)
                .build();
    }
}
