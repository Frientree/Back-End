package com.d101.frientree.entity.leaf;

import com.d101.frientree.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "leaf_receive")
@Data
@Getter
@Setter
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


    public static LeafReceive createLeafReceive(LeafDetail selectedLeaf, User user) {
        LeafReceive leafReceive = new LeafReceive();
        leafReceive.setUser(user);
        leafReceive.setLeafDetail(selectedLeaf);
        return leafReceive;
    }


}
