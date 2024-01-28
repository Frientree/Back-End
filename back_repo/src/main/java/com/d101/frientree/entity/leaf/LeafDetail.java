package com.d101.frientree.entity.leaf;

import com.d101.frientree.dto.leaf.request.LeafGenerationRequest;
import com.d101.frientree.entity.LeafCategory;
import com.d101.frientree.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

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

    @JsonIgnore
    @OneToMany(mappedBy = "leafDetail")
    private List<LeafReceive> leafReceives;

    @JsonIgnore
    @OneToMany(mappedBy = "leafDetail")
    private List<LeafSend> leafSends;

    public static LeafDetail createLeafDetail(LeafGenerationRequest leafGenerationRequest) {
        return LeafDetail.builder()
                .leafCategory(leafGenerationRequest.getLeafCategory())
                .leafContent(leafGenerationRequest.getLeafContent())
                .build();
    }
}