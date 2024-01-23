package com.d101.frientree.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JuiceDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "juice_num")
    private Long juiceNum;

    @Column(name = "juice_name")
    private String juiceName;

    @Column(name = "juice_image_url")
    private String juiceImageUrl;

    @Column(name = "juice_info")
    private String juiceInfo;

    @Column(name = "juice_min_score")
    private Long juiceMinScore;

    @Column(name = "juice_max_score")
    private Long juiceMaxScore;
}
