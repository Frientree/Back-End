package com.d101.frientree.repository.mongo;

import com.d101.frientree.entity.mongo.emotion.Emotion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoEmotionRepository extends MongoRepository<Emotion, String> {
    //가장 최근 저장된 유저 데이터 가져오기
    Optional<Emotion> findTopByUserPKOrderByTimestampDesc(String userPK);

}
