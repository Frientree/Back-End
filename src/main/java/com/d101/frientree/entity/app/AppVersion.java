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

    @Column(name = "app_min_version")
    private String appMinVersion;
}
