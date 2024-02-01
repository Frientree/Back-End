package com.d101.frientree.dto.juice.response.dto;

import com.d101.frientree.entity.juice.JuiceDetail;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JuiceConfirmationResponseDTO {

    private String juiceImageUrl;
    private String juiceName;
    private String juiceDescription;

    public static JuiceConfirmationResponseDTO createJuiceConfirmationResponseDTO(JuiceDetail juiceDetail) {
        return JuiceConfirmationResponseDTO.builder()
                .juiceImageUrl(juiceDetail.getJuiceImageUrl())
                .juiceName(juiceDetail.getJuiceName())
                .juiceDescription(juiceDetail.getJuiceInfo())
                .build();
    }
}
