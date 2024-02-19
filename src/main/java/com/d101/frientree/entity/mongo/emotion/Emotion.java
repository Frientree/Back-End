package com.d101.frientree.entity.mongo.emotion;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "emotion")
public class Emotion {
    @Id
    private String id;

    private String userPK;
    private String text;
    private String emotionResult;
    private Instant timestamp; // 문서의 생성 또는 수정 시간을 기록하기 위한 필드

    public Emotion(String userPK, String text, String emotionResult, Instant timestamp) {
        this.userPK = userPK;
        this.text = text;
        this.emotionResult = emotionResult;
        this.timestamp = timestamp;
    }
}
