package com.d101.frientree.dto.leaf.response.dto;

import com.d101.frientree.entity.LeafDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeafViewResponseDTO {

    private Long views;


    public static LeafViewResponseDTO createLeafViewDTO(LeafDetail leafDetail) {
        return LeafViewResponseDTO.builder()
                .views(null)
                .build();
    }
}
