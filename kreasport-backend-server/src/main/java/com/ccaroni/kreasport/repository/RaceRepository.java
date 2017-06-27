package com.ccaroni.kreasport.repository;

import com.ccaroni.kreasport.dto.Race;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Master on 16/04/2017.
 */
@Repository
public interface RaceRepository extends MongoRepository<Race, String> {

}