package com.d101.frientree.dto.juice.response;

import com.d101.frientree.dto.juice.response.dto.JuiceListConfirmationResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JuiceListConfirmationResponse {

    private String message;
    private List<JuiceListConfirmationResponseDTO> data;

    public static JuiceListConfirmationResponse createJuiceListConfirmationResponse(String message, List<JuiceListConfirmationResponseDTO> dto) {

        return JuiceListConfirmationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
