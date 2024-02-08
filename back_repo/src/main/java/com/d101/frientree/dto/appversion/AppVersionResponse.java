package com.d101.frientree.dto.appversion;

import com.d101.frientree.dto.appversion.dto.AppVersionResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Builder
@AllArgsConstructor
@Data
public class AppVersionResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private AppVersionResponseDTO data;

    public static AppVersionResponse createAppVersionResponse(String message, AppVersionResponseDTO dto) {
        return AppVersionResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
