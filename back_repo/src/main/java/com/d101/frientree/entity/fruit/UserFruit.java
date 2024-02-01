package com.d101.frientree.entity.fruit;

import com.d101.frientree.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_FRUIT")
public class UserFruit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userFruitNum;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fruitNum")
    private FruitDetail fruitDetail;

    @Temporal(TemporalType.DATE)
    private Date userFruitCreateDate;

    private Long userFruitScore;

    public static UserFruit createUserFruit(User user, FruitDetail fruitDetail, Date userFruitCreateDate, Long userFruitScore) {
        return UserFruit.builder()
                .user(user)
                .fruitDetail(fruitDetail)
                .userFruitCreateDate(userFruitCreateDate)
                .userFruitScore(userFruitScore)
                .build();
    }

}
