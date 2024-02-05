package com.d101.frientree.dto.juice.response.dto;

import com.d101.frientree.entity.juice.JuiceDetail;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JuiceListConfirmationResponseDTO {

    private Long juiceNum;
    private String juiceImageUrl;
    private String juiceName;
    private Boolean juiceOwn;
    private String juiceDescription;

    public static JuiceListConfirmationResponseDTO createJuiceListConfirmationResponseDTO(JuiceDetail juiceDetail, Boolean own) {

        return JuiceListConfirmationResponseDTO.builder()
                .juiceName(juiceDetail.getJuiceName())
                .juiceNum(juiceDetail.getJuiceNum())
                .juiceImageUrl(juiceDetail.getJuiceImageUrl())
                .juiceOwn(own)
                .juiceDescription(juiceDetail.getJuiceInfo())
                .build();
    }
}
