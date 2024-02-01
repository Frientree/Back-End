package com.d101.frientree.dto.juice.response.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JuiceGenerationResponseDTO {

    private List<JuiceFruitsGraphDataDTO> dto;

    public static JuiceGenerationResponseDTO createJuiceGenerationResponseDTO(List<JuiceFruitsGraphDataDTO> dto) {
        return JuiceGenerationResponseDTO.builder()
                .dto(dto)
                .build();
    }
}
