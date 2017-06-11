package com.ccaroni.kreasport.repository;

import com.ccaroni.kreasport.domain.RaceRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Master on 30/05/2017.
 */
@Repository
public interface RaceRecordRepository extends MongoRepository<RaceRecord, String> {

    RaceRecord findById(String id);

    void deleteById(String id);
}
