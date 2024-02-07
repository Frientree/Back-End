package com.d101.frientree.dto.appversion.dto;

import com.d101.frientree.entity.app.AppVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppVersionResponseDTO {
    private boolean appAvailable;
    private String url;
    private String minVersion;
    private static String updateUrl = "www.naver.com";

    public static AppVersionResponseDTO createAppVersionResponseDTO(AppVersion appVersion, boolean appAvailable) {
        return AppVersionResponseDTO.builder()
                .appAvailable(appAvailable)
                .url(updateUrl)
                .minVersion(appVersion.getAppMinVersion())
                .build();
    }
}
