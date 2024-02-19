package com.d101.frientree.service.mongo;

import java.io.IOException;

public interface MongoEmotionService {
    void createEmotion(String userPK, String text, String emotionResult);

    String makeFileCsv() throws IOException;

    void deleteEmotion();
}
