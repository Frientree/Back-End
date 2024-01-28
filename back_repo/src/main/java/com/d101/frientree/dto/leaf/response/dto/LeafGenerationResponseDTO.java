package com.d101.frientree.dto.leaf.response.dto;

import com.d101.frientree.entity.leaf.LeafDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeafGenerationResponseDTO {

    private boolean isCreated;


    public static LeafGenerationResponseDTO createLeafGenerationresponseDTO(LeafDetail leafDetail) {
        return LeafGenerationResponseDTO.builder()
                .isCreated(leafDetail != null)
                .build();
    }
}