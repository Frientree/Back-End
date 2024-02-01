package com.d101.frientree.dto.juice.response;

import com.d101.frientree.dto.juice.response.dto.JuiceListConfirmationResponseDTO;
import com.d101.frientree.entity.juice.JuiceDetail;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JuiceListConfirmationResponse {

    private String message;
    private List<JuiceListConfirmationResponseDTO> dto;

    public static JuiceListConfirmationResponse createJuiceListConfirmationResponse(String message, List<JuiceListConfirmationResponseDTO> dto) {

        return JuiceListConfirmationResponse.builder()
                .message(message)
                .dto(dto)
                .build();
    }
}
