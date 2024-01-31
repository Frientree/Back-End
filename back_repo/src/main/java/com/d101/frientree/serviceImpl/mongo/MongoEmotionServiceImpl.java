package com.d101.frientree.serviceImpl.mongo;

import com.d101.frientree.entity.mongo.emotion.Emotion;
import com.d101.frientree.repository.mongo.MongoEmotionRepository;
import com.d101.frientree.service.mongo.MongoEmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MongoEmotionServiceImpl implements MongoEmotionService {
    private final MongoEmotionRepository mongoEmotionRepository;

    public void createEmotion(String userPK, String text, String emotionResult){
        Emotion emotion = new Emotion(userPK, text, emotionResult, Instant.now());
        mongoEmotionRepository.save(emotion);
    }
}
