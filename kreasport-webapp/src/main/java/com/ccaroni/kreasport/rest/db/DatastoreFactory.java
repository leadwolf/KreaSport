package com.ccaroni.kreasport.rest.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class DatastoreFactory {

    final static Logger logger = LoggerFactory.getLogger(DatastoreFactory.class);

    private static MongoClient client = null;

    public static Datastore getDB(boolean test) {
        if (client == null) {
            String MONGO_URI = System.getenv("MONGO_URI");
            logger.info("uri = " + MONGO_URI);
            MongoClientURI uri = new MongoClientURI(MONGO_URI);
            client = new MongoClient(uri);
        }
        Morphia morphia = new Morphia();
        Datastore ds = null;
        if (test) {
            ds = morphia.createDatastore(client, "test");
        } else {
            ds = morphia.createDatastore(client, "kreasport-database");
        }
        ds.ensureIndexes();
        return ds;
    }

}