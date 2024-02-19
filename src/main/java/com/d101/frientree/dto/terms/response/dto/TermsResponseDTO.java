package com.d101.frientree.dto.terms.response.dto;

import com.d101.frientree.entity.app.Terms;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermsResponseDTO {
    @Schema(description = "약관 URL", example = "www.ccc.com")
    private String url;
    @Schema(description = "약관 제목", example = "프랜트리 약관입니다.")
    private String name;
    @Schema(description = "필수 약관 여부", example = "true")
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
