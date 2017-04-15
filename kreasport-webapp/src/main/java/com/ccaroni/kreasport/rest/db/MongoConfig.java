package com.ccaroni.kreasport.rest.db;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by Master on 15/04/2017.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.ccaroni.kreasport.rest.api")
public class MongoConfig extends AbstractMongoConfiguration {

    private static MongoClient mongoClient = null;

    @Override
    protected String getDatabaseName() {
        return "kreasport-database";
    }

    @Override
    public Mongo mongo() throws Exception {
        return getMongoClient();
    }
    public static MongoClient getMongoClient() {
        if (mongoClient == null) {
            String MONGO_URI = System.getenv("MONGO_URI");
            MongoClientURI uri = new MongoClientURI(MONGO_URI);
            mongoClient = new MongoClient(uri);
        }
        return mongoClient;
    }
}
