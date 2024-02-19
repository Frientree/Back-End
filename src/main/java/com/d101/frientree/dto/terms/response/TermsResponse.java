package com.d101.frientree.dto.terms.response;

import com.d101.frientree.dto.terms.response.dto.TermsResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;  // List 타입을 import 추가

@Builder
@AllArgsConstructor
@Data
public class TermsResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private List<TermsResponseDTO> data;

    public static TermsResponse createTermsResponse(String message, List<TermsResponseDTO> dto) {
        return TermsResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
