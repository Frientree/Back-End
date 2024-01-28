package com.d101.frientree.dto.leaf.response.dto;


import com.d101.frientree.dto.user.response.dto.UserConfirmationResponseDTO;
import com.d101.frientree.entity.LeafCategory;
import com.d101.frientree.entity.LeafDetail;
import com.d101.frientree.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeafConfirmationResponseDTO {

    private Long leafNum;

    private String leafContent;



    public static LeafConfirmationResponseDTO createLeafConfirmationResponseDTO(LeafDetail leafDetail) {
        return LeafConfirmationResponseDTO.builder()
                .leafNum(leafDetail.getLeafNum())
                .leafContent(leafDetail.getLeafContent())
                .build();
    }

}
