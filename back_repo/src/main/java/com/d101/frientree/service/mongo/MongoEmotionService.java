package com.d101.frientree.service.mongo;

public interface MongoEmotionService {
    void createEmotion(String userPK, String text, String emotionResult);
}
