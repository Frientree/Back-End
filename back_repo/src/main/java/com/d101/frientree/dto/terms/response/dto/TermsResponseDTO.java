package com.d101.frientree.dto.terms.response.dto;

import com.d101.frientree.entity.app.Terms;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermsResponseDTO {
    private String url;
    private String name;

    public static TermsResponseDTO createTermsResponseDTO(Terms terms) {
        return TermsResponseDTO.builder()
                .url(terms.getTermsUrl())
                .name(terms.getTermsTitle())
                .build();
    }
}
