package com.d101.frientree.entity.message;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_num")
    private Long messageNum;

    @Column(name = "message_description")
    private String messageDescription;
}
