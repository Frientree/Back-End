package com.d101.frientree.service.app;

import com.d101.frientree.dto.appversion.AppVersionResponse;
import org.springframework.http.ResponseEntity;

public interface AppVersionService {
    ResponseEntity<AppVersionResponse> confirm();
}
