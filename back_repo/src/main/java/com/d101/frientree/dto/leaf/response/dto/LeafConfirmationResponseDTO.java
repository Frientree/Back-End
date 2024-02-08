package com.d101.frientree.dto.leaf.response.dto;


import com.d101.frientree.entity.leaf.LeafDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeafConfirmationResponseDTO {
    @Schema(description = "이파리 번호", example = "1")
    private Long leafNum;
    @Schema(description = "이파리 내용", example = "가나다라")
    private String leafContent;

    public static LeafConfirmationResponseDTO createLeafConfirmationResponseDTO(LeafDetail leafDetail) {
        return LeafConfirmationResponseDTO.builder()
                .leafNum(leafDetail.getLeafNum())
                .leafContent(leafDetail.getLeafContent())
                .build();
    }

    public static LeafConfirmationResponseDTO createLeafConfirmationResponseDTO(String description) {
        return LeafConfirmationResponseDTO.builder()
                .leafNum(0L)
                .leafContent(description)
                .build();
    }

}
