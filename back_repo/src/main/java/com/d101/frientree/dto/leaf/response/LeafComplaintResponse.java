package com.d101.frientree.dto.leaf.response;

import com.d101.frientree.dto.leaf.response.dto.LeafComplaintResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeafComplaintResponse {

    private String message;
    private Boolean data;
    public static LeafComplaintResponse createLeafComplaintResponse(String message, Boolean isComplained) {
        return LeafComplaintResponse.builder()
                .message(message)
                .data(isComplained)
                .build();
    }
}
