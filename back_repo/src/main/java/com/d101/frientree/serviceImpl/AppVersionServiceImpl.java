package com.d101.frientree.serviceImpl;

import com.d101.frientree.dto.appversion.AppVersionResponse;
import com.d101.frientree.dto.appversion.dto.AppVersionResponseDTO;
import com.d101.frientree.entity.app.AppVersion;
import com.d101.frientree.repository.AppVersionRespository;
import com.d101.frientree.service.AppVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppVersionServiceImpl implements AppVersionService {

    private final AppVersionRespository appVersionRespository;


    @Override
    public ResponseEntity<AppVersionResponse> confirm() {

        List<AppVersion> appVersions = appVersionRespository.findAll();

        AppVersion appVersion = appVersions.get(0);

        AppVersionResponse response = AppVersionResponse.createAppVersionResponse(
                "Success", AppVersionResponseDTO.createAppVersionResponseDTO(appVersion)
        );

        return ResponseEntity.ok(response);
    }

}
