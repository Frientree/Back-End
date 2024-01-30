package com.d101.frientree.serviceImpl.mongo;

import com.d101.frientree.entity.mongo.emotion.Emotion;
import com.d101.frientree.repository.mongo.MongoEmotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoEmotionServiceImpl {
    private final MongoEmotionRepository mongoEmotionRepository;

    public Emotion createEmotion(String userEmail, String text, String emotionResult){
        Emotion emotion = new Emotion(userEmail, text, emotionResult);
        return mongoEmotionRepository.save(emotion);
    }
}
