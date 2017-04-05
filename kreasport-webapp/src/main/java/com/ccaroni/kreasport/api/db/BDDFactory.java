package com.ccaroni.kreasport.api.db;

import org.skife.jdbi.v2.DBI;

import javax.inject.Singleton;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

@Singleton
public class BDDFactory {
    private static DBI dbi = null;

    public static DBI getDbi() {
        if (dbi == null) {

            URI dbUri = null;
            try {
                dbUri = new URI(System.getenv("DATABASE_URL"));

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                dbi = new DBI(dbUrl, username, password);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
        return dbi;
    }

    public static boolean tableExist(String tableName) throws SQLException, URISyntaxException {
        DatabaseMetaData dbm = getDbi().open().getConnection().getMetaData();
        ResultSet tables = dbm.getTables(null, null, tableName, null);
        boolean exist = tables.next();
        tables.close();
        return exist;
    }
}
