package com.d101.frientree.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FruitDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fruit_num")
    private Long fruitNum;

    @Column(name = "fruit_name")
    private String fruitName;

    @Column(name = "fruit_image_url")
    private String fruitImageUrl;

    @Column(name = "fruit_feel")
    private String fruitFeel;

    @Column(name = "fruit_min_score")
    private Long fruitMinScore;

    @Column(name = "fruit_max_score")
    private Long fruitMaxScore;

    @Column(name = "fruit_description")
    private String fruitDescription;

    @Column(name = "fruit_calendar_url")
    private String fruitCalendarUrl;

}
