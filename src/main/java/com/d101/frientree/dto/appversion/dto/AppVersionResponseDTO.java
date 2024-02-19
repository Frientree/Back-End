package com.d101.frientree.dto.appversion.dto;

import com.d101.frientree.entity.app.AppVersion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppVersionResponseDTO {
    @Schema(description = "앱 스토어 링크", example = "www.bbb.com")
    private String url;
    @Schema(description = "앱 최소 실행 버전", example = "Success")
    private String minVersion;

    private static String updateUrl = "https://play.google.com/store/apps/details?id=com.d101.frientree";

    public static AppVersionResponseDTO createAppVersionResponseDTO(AppVersion appVersion) {
        return AppVersionResponseDTO.builder()
                .url(updateUrl)
                .minVersion(appVersion.getAppMinVersion())
                .build();
    }
}
