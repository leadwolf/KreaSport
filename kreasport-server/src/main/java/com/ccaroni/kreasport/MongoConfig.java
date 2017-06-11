package com.ccaroni.kreasport;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * Created by Master on 11/06/2017.
 */

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "kreasport-mongodb";
    }

    @Override
    public Mongo mongo() throws Exception {
        String MONGO_URI = System.getenv("MONGO_URI");
        MongoClientURI uri = new MongoClientURI(MONGO_URI);
        return new MongoClient(uri);
    }

}