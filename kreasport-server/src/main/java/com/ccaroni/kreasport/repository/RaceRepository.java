package com.ccaroni.kreasport.repository;

import com.ccaroni.kreasport.domain.Race;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Master on 16/04/2017.
 */
@Repository
public interface RaceRepository extends MongoRepository<Race, String> {

    Race findById(String id);

    void deleteById(String id);
}