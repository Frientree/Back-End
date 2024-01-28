package com.d101.frientree.entity.fruit;

import com.d101.frientree.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Entity
@Data
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

    private BigInteger userFruitScore;

}
