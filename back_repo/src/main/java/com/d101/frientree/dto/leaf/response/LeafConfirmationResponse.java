package com.d101.frientree.dto.leaf.response;

import com.d101.frientree.dto.leaf.response.dto.LeafConfirmationResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeafConfirmationResponse {


    private String message;
    private LeafConfirmationResponseDTO data;

    public static LeafConfirmationResponse createLeafConfirmationResponse(String message, LeafConfirmationResponseDTO dto) {
        return com.d101.frientree.dto.leaf.response.LeafConfirmationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
