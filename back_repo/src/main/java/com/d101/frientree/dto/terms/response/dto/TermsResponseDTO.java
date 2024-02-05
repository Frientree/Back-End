package com.d101.frientree.dto.terms.response.dto;

import com.d101.frientree.entity.app.Terms;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermsResponseDTO {
    private String url;
    private String name;
    private boolean necessary ;

    public static TermsResponseDTO createTermsResponseDTO(Terms terms) {
        String title = terms.getIsNecessary() ? "[필수] " + terms.getTermsTitle() : terms.getTermsTitle();
        return TermsResponseDTO.builder()
                .url(terms.getTermsUrl())
                .name(title)
                .necessary(terms.getIsNecessary())
                .build();
    }
}
