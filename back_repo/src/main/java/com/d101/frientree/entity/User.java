package com.d101.frientree.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "user_email", unique = true)
    private String userEmail;

    @Builder.Default
    @Column(name = "user_disabled")
    private Boolean userDisabled = false;

    @Column(name = "user_create_date")
    private Date userCreateDate;

    @Column(name = "user_login_type")
    private Long userLoginType;

    @Builder.Default
    @Column(name = "user_leaf_status")
    private Boolean userLeafStatus = true;

    @Builder.Default
    @Column(name = "user_notification")
    private Boolean userNotification = false;

    @Builder.Default
    @Column(name = "user_fruit_status")
    private Boolean userFruitStatus = true;

//    @Builder.Default
//    @Column(name = "user_enabled")
//    private Boolean userEnabled = true;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "leaf_send",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "leaf_num"))
    private Set<LeafDetail> leafSend = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "leaf_receive",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "leaf_num"))
    private Set<LeafDetail> leafReceive = new HashSet<>();

}
