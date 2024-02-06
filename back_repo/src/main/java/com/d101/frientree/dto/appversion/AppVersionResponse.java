package com.d101.frientree.dto.appversion;

import com.d101.frientree.dto.appversion.dto.AppVersionResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Builder
@AllArgsConstructor
@Data
public class AppVersionResponse {

    private String message;
    private AppVersionResponseDTO data;

    public static AppVersionResponse createAppVersionResponse(String message, AppVersionResponseDTO dto) {
        return AppVersionResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
