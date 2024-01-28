package com.d101.frientree.entity.app;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppVersion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_version_num")
    private Long appVersionNum;

    @Column(name = "app_pre_version")
    private String appPreVersion;

    @Column(name = "app_now_version")
    private String appNowVersion;
}
