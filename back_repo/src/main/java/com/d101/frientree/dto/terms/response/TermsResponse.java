package com.d101.frientree.dto.terms.response;

import com.d101.frientree.dto.terms.response.dto.TermsResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;  // List 타입을 import 추가

@Builder
@AllArgsConstructor
@Data
public class TermsResponse {

    private String message;
    private List<TermsResponseDTO> data;  // 수정: List 타입으로 변경

    public static TermsResponse createTermsResponse(String message, List<TermsResponseDTO> dto) {
        return TermsResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
