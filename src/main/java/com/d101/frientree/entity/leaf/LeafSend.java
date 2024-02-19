package com.d101.frientree.entity.leaf;

import com.d101.frientree.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "leaf_send")
@Getter
@Setter
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


    public static LeafSend createLeafSend(LeafDetail leafDetail, User user) {
        LeafSend leafSend = new LeafSend();
        leafSend.setLeafDetail(leafDetail);
        leafSend.setUser(user);
        return leafSend;
    }


}
