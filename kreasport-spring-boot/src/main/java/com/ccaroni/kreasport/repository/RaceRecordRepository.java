package com.ccaroni.kreasport.repository;

import com.ccaroni.kreasport.dto.RaceRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Master on 30/05/2017.
 */
@Repository
public interface RaceRecordRepository extends MongoRepository<RaceRecord, String> {
}
