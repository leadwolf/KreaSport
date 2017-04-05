package com.ccaroni.kreasport.api.db;

import com.ccaroni.kreasport.api.pojo.Checkpoint;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;

import java.util.List;

/**
 * Created by Master on 04/04/2017.
 */
public interface CheckpointDAO {
    @SqlUpdate("create table checkpoints (id serial primary key, raceId integer, title varchar(40), description text, latitude real, longitude real, question varchar(64))")
    void createCheckpointTable();

    @SqlUpdate("insert into checkpoints (raceId, title, description, latitude, longitude, question) values (:c.raceId, :c.title, :c.description, :c.latitude, :c.longitude, :c" +
            ".question)")
    @GetGeneratedKeys
    int insert(@BindBean("c") Checkpoint checkpoint);

    @SqlQuery("select * from checkpoints where id = :id and raceId = :raceId")
    @RegisterMapperFactory(BeanMapperFactory.class)
    Checkpoint findByID(@Bind("id") int id, @Bind("raceId") int raceId);

    @SqlQuery("select * from checkpoints where raceId = :raceId order by id")
    @RegisterMapperFactory(BeanMapperFactory.class)
    List<Checkpoint> findByRaceID(@Bind("raceId") int raceId);

    @SqlQuery("select * from checkpoints")
    List<Checkpoint> all();

    void dropCheckpointsTable();


    @SqlUpdate("delete from checkpoints where raceId = :raceId")
    void deleteAllForRace(@Bind("raceId") int raceId);

    @SqlUpdate("delete from checkpoints where id = :id and raceId = :raceId")
    void delete(@Bind("raceId") int raceId, @Bind("id") int id);

    @SqlUpdate("drop table if exists checkpoints")

    void close();

}
