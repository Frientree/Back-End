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
public class LeafViewResponseDTO {

    private Long views;
    private Long totalLeafView;


    public static LeafViewResponseDTO createLeafViewResponseDTO(LeafDetail leafDetail) {
        return LeafViewResponseDTO.builder()
                .views(leafDetail.getLeafView()) // LeafDetail에서 조회수 정보를 가져와 설정
                .build();
    }

}
