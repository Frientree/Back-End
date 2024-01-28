package com.d101.frientree.entity.leaf;

import com.d101.frientree.entity.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "leaf_receive")
public class LeafReceive {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leaf_receive_num")
    private Long leafReceiveNum;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "leafNum")
    private LeafDetail leafDetail;
}
