package com.d101.frientree.entity;

import com.d101.frientree.dto.leaf.request.LeafGenerationRequest;
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

    @ManyToMany(mappedBy = "leafSend")
    private List<User> sentByUsers = new ArrayList<>();

    // LeafDetail 클래스에 List 필드 추가
    @ManyToMany(mappedBy = "leafReceive")
    private List<User> receivedByUsers = new ArrayList<>();

    public static LeafDetail createLeafDetail(LeafGenerationRequest leafGenerationRequest) {
        return LeafDetail.builder()
                .leafCategory(leafGenerationRequest.getLeafCategory())
                .leafContent(leafGenerationRequest.getLeafContent())
                .build();
    }

}
