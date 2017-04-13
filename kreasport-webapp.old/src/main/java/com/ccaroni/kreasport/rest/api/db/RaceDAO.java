package com.ccaroni.kreasport.rest.api.db;

import com.ccaroni.kreasport.rest.api.pojo.Race;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;

/**
 * Created by Master on 04/04/2017.
 */
public interface RaceDAO {

    @SqlUpdate("create table races (id serial primary key, title varchar(40), description text, latitude real, longitude real, question varchar(64)")
    void createRaceTable();

    @SqlUpdate("insert into races values (:id, :title, :description, :latitude, :longitude, :question")
    @GetGeneratedKeys
    int insertRace(@BindBean Race race);

    @SqlQuery("select * from races where id = :id")
    @RegisterMapperFactory(BeanMapperFactory.class)
    Race findById(@Bind("id") int id);

    @SqlUpdate("delete from races where id = :id")
    void delete(@Bind("id") int id);

    @SqlUpdate("drop table if exists races")
    void dropRacesTable();

    void close();


}
