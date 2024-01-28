package com.d101.frientree.entity.leaf;

import com.d101.frientree.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "leaf_send")
public class LeafSend {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leaf_send_num")
    private Long leafSendNum;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "leafNum")
    private LeafDetail leafDetail;

    @Builder
    public static LeafSend createLeafSend(LeafDetail leafDetail, User user){
        return LeafSend.builder()
                .leafDetail(leafDetail)
                .user(user)
                .build();
    }

}
