package com.d101.frientree.repository.app;

import com.d101.frientree.entity.app.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppVersionRespository extends JpaRepository<AppVersion, Long> {
}
