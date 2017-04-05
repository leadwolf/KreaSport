package com.ccaroni.kreasport.api.db;

import com.ccaroni.kreasport.api.pojo.PossibleAnswer;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;

import java.util.List;

/**
 * Created by Master on 04/04/2017.
 */
public interface PossibleAnswerDAO {

    @SqlUpdate("create table answers (id serial primary key, checkpointId integer, answer varchar(50)")
    void createPossibleAnswersTable();

    @SqlUpdate("insert into answers values (:id, :checkpointId, :possibleAnswer")
    @GetGeneratedKeys
    int insertAnswer(@BindBean() PossibleAnswer possibleAnswer);

    @SqlQuery("select answer from answers where id = :id and checkpointId = :checkpointId")
    @RegisterMapperFactory(BeanMapperFactory.class)
    PossibleAnswer findByIdAndCheckpintId(@Bind("id") int id, @Bind("checkpointId") int checkpointId);


    @SqlQuery("select answer from answers where checkpointId = :checkpointId")
    @RegisterMapperFactory(BeanMapperFactory.class)
    List<PossibleAnswer> findByCheckpointId(@Bind("checkpointId") int checkpointId);

    @SqlUpdate("delete from answers where id = :id")
    void delete(@Bind("id") int id);

    @SqlUpdate("drop table if exists answers")
    void dropRacesTable();

    void close();
}
