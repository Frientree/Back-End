package com.d101.frientree.entity.juice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JuiceMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long juiceMessageNum;

    @ManyToOne
    @JoinColumn(name = "juiceNum")
    private JuiceDetail juiceDetail;

    @Column(name = "juice_message")
    private String juiceMessage;
}
