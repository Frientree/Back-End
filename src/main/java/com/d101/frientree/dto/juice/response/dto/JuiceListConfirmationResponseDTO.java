package com.d101.frientree.dto.juice.response.dto;

import com.d101.frientree.entity.juice.JuiceDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JuiceListConfirmationResponseDTO {

    @Schema(description = "주스 번호", example = "1")
    private Long juiceNum;
    @Schema(description = "주스 이미지 URL", example = "www.efgfh.com")
    private String juiceImageUrl;
    @Schema(description = "주스 이름", example = "사과 주스")
    private String juiceName;
    @Schema(description = "주스 보유 여부", example = "true")
    private Boolean juiceOwn;
    @Schema(description = "주스 세부 내용", example = "달콤하고 바삭한 사과의 맛이 어우러진 사과 주스입니다. 상큼하면서도 과일 특유의 달콤한 맛이 느껴집니다.")
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
