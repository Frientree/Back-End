package com.d101.frientree.repository.mongo;

import com.d101.frientree.entity.mongo.emotion.Emotion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoEmotionRepository extends MongoRepository<Emotion, String> {

}
