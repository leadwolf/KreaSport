package com.ccaroni.kreasport.rest.db;

import com.ccaroni.kreasport.rest.api.Race;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Master on 15/04/2017.
 */
public interface RaceRepository extends MongoRepository<Race, String> {

    List<Race> findByLocationNear(Point point, Distance max);

    List<Race> findByLocationWithin(Circle circle);

    List<Race> findByLocationWithin(Box box);

}
