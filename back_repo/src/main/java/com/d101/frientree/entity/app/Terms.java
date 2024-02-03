package com.d101.frientree.entity.app;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Terms {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_num")
    private Long termsNum;

    @Column(name = "terms_title")
    private String termsTitle;

    @Column(name = "terms_url")
    private String termsUrl;
}
