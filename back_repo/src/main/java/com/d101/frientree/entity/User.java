package com.d101.frientree.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

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

}
