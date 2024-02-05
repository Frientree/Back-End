package com.d101.frientree.repository.mongo;

import com.d101.frientree.entity.mongo.leaf.Leaf;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoLeafRepository extends MongoRepository<Leaf, String> {

}
