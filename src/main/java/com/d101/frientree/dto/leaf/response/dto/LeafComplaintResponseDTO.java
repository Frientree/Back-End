package com.d101.frientree.dto.leaf.response.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LeafComplaintResponseDTO {

    private boolean isDeleted;

    public static LeafComplaintResponseDTO createLeafComplaintResponseDTO() {
        return LeafComplaintResponseDTO.builder()
                .isDeleted(true)
                .build();
    }
}
