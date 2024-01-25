package com.d101.frientree.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LeafDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leaf_num")
    private Long leafNum;

    @Column(name = "leaf_content")
    private String leafContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "leaf_category")
    private LeafCategory leafCategory;

    @Column(name = "leaf_complain")
    @Builder.Default
    private Long leafComplain = 0l;

    @Column(name = "leaf_view")
    @Builder.Default
    private Long leafView = 0l;

    @Column(name = "leaf_create_date")
    private Date leafCreateDate;

}
