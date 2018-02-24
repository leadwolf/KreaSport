package com.ccaroni.kreasport.explore.di;

import com.ccaroni.kreasport.data.local.Checkpoint;
import com.ccaroni.kreasport.data.local.Race;
import com.ccaroni.kreasport.data.local.Record;
import com.ccaroni.kreasport.explore.model.IRaceModel;
import com.ccaroni.kreasport.explore.model.impl.RaceModel;

import dagger.Module;
import dagger.Provides;
import io.objectbox.Box;

/**
 * Created by Master on 24/02/2018.
 */
@Module
public class ExploreModule {

    @ExploreScope
    @Provides
    public IRaceModel providesIRaceModel(Box<Race> raceBox, Box<Checkpoint> checkpointBox, Box<Record> recordBox) {
        return new RaceModel(raceBox, checkpointBox, recordBox);
    }

}
