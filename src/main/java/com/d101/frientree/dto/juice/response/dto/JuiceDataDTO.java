package com.d101.frientree.dto.juice.response.dto;

import com.d101.frientree.entity.juice.JuiceDetail;
import com.d101.frientree.entity.message.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JuiceDataDTO {
    @Schema(description = "주스 이름", example = "사과 주스")
    private String juiceName;
    @Schema(description = "주스 이미지 URL", example = "www.efgfh.com")
    private String juiceImageUrl;
    @Schema(description = "주스 세부 내용", example = "달콤하고 바삭한 사과의 맛이 어우러진 사과 주스입니다. 상큼하면서도 과일 특유의 달콤한 맛이 느껴집니다.")
    private String juiceDescription;
    @Schema(description = "위로 메시지", example = "목표 없는 배낭여행은 그냥 쓸모없는 짐이다.")
    private String condolenceMessage;

    public static JuiceDataDTO createJuiceDataDTO(JuiceDetail juiceDetail, String message) {
        return JuiceDataDTO.builder()
                .juiceDescription(juiceDetail.getJuiceInfo())
                .juiceName(juiceDetail.getJuiceName())
                .juiceImageUrl(juiceDetail.getJuiceImageUrl())
                .condolenceMessage(message)
                .build();
    }
}
