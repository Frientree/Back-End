package com.d101.frientree.dto.juice.response.dto;

import com.d101.frientree.entity.juice.JuiceDetail;
import com.d101.frientree.entity.message.Message;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JuiceDataDTO {

    private String juiceName;
    private String juiceImageUrl;
    private String juiceDescription;
    private String condolenceMessage;

    public static JuiceDataDTO createJuiceDataDTO(JuiceDetail juiceDetail, Message message) {
        return JuiceDataDTO.builder()
                .juiceDescription(juiceDetail.getJuiceInfo())
                .juiceName(juiceDetail.getJuiceName())
                .juiceImageUrl(juiceDetail.getJuiceImageUrl())
                .condolenceMessage(message.getMessageDescription())
                .build();
    }
}
