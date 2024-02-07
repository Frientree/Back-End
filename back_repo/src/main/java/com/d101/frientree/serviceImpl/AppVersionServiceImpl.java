package com.d101.frientree.serviceImpl;

import com.d101.frientree.dto.appversion.AppVersionResponse;
import com.d101.frientree.dto.appversion.dto.AppVersionResponseDTO;
import com.d101.frientree.entity.app.AppVersion;
import com.d101.frientree.repository.AppVersionRespository;
import com.d101.frientree.service.AppVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppVersionServiceImpl implements AppVersionService {

    private final AppVersionRespository appVersionRespository;


    @Override
    public ResponseEntity<AppVersionResponse> confirm() {
        //현재 시간 가져오기
        LocalTime now = LocalTime.now();

        //00:00과 00:10을 나타내는 LocalTime 객체를 생성
        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(0, 10);

        boolean appAvailable = !(now.isAfter(startTime) && now.isBefore(endTime) || now.equals(startTime));

        List<AppVersion> appVersions = appVersionRespository.findAll();

        AppVersion appVersion = appVersions.get(0);

        AppVersionResponse response = AppVersionResponse.createAppVersionResponse(
                "Success",
                AppVersionResponseDTO.createAppVersionResponseDTO(appVersion, appAvailable)
        );

        return ResponseEntity.ok(response);
    }

}
