package com.d101.frientree.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "user_disabled")
    private Boolean userDisabled;

    @Column(name = "user_create_date")
    private Date userCreateDate;

    @Column(name = "user_login_type")
    private Long userLoginType;

    @Column(name = "user_leaf_status")
    private Boolean userLeafStatus;

    @Column(name = "user_notification")
    private Boolean userNotification;

    @Column(name = "user_fruit_status")
    private Boolean userFruitStatus;

}
