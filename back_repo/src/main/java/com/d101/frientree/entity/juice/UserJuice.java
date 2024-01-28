package com.d101.frientree.entity.juice;

import com.d101.frientree.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJuice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userJuiceNum;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "juiceNum")
    private JuiceDetail juiceDetail;

    @Temporal(TemporalType.DATE)
    private Date userJuiceCreateDate;
}
