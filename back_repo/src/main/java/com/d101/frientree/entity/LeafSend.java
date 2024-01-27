package com.d101.frientree.entity;

import jakarta.persistence.*;

@Entity
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

}
