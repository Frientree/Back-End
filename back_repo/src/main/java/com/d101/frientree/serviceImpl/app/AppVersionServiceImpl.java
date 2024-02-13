package com.d101.frientree.serviceImpl.app;

import com.d101.frientree.dto.appversion.AppVersionResponse;
import com.d101.frientree.dto.appversion.dto.AppVersionResponseDTO;
import com.d101.frientree.entity.app.AppVersion;
import com.d101.frientree.repository.app.AppVersionRespository;
import com.d101.frientree.service.app.AppVersionService;
import com.d101.frientree.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppVersionServiceImpl implements AppVersionService {

    private final AppVersionRespository appVersionRespository;
    private final CommonUtil commonUtil;

    @Override
    public ResponseEntity<AppVersionResponse> confirm() {
        commonUtil.checkServerInspectionTime();

        List<AppVersion> appVersions = appVersionRespository.findAll();

        AppVersion appVersion = appVersions.get(0);

        AppVersionResponse response = AppVersionResponse.createAppVersionResponse(
                "Success",
                AppVersionResponseDTO.createAppVersionResponseDTO(appVersion)
        );

        return ResponseEntity.ok(response);
    }

}
