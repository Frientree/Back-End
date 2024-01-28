package com.d101.frientree.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

// jointable 을 통해 중계 테이블을 만들면 PK 설정을 할 수 없다고 함,
// pk 설정을 위해 별도로 테이블을 생성해서 manytoone 으로 설정함
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "leaf_send")
public class LeafSend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leaf_send_num")
    private Long leafSendNum;

    @ManyToOne
    @JoinColumn(name = "leaf_num", referencedColumnName = "leaf_num")
    private LeafDetail leafDetail;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public static LeafSend createLeafSend(LeafDetail leafDetail, User user){
        return LeafSend.builder()
                .leafDetail(leafDetail)
                .user(user)
                .build();
    }

}
