package com.d101.frientree.entity.mongo.emotion;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "emotion")
public class Emotion {
    @Id
    private String Id;

    private String userEmail;
    private String text;
    private String emotionResult;

    public Emotion(String userEmail, String text, String emotionResult) {
    }
}
