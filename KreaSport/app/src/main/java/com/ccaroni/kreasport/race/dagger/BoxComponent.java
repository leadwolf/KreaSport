package com.ccaroni.kreasport.race.dagger;

import com.ccaroni.kreasport.data.local.Checkpoint;
import com.ccaroni.kreasport.data.local.DownloadedArea;
import com.ccaroni.kreasport.data.local.Race;
import com.ccaroni.kreasport.data.local.Record;
import com.ccaroni.kreasport.race.view.fragments.ExploreFragment;
import com.ccaroni.kreasport.race.vm.AbstractRaceVM;
import com.ccaroni.kreasport.race.vm.impl.RaceVM;

import javax.inject.Singleton;

import dagger.Component;
import io.objectbox.Box;

/**
 * Created by Master on 17/02/2018.
 */

@Singleton
@Component(modules = {AppModule.class, BoxStoreModule.class})
public interface BoxComponent {

    void inject(ExploreFragment exploreFragment);

    void inject(AbstractRaceVM abstractRaceVM);

    void inject(RaceVM raceVM);

    Box<Race> getRaceBox();

    Box<Checkpoint> getCheckpointBox();

    Box<Record> getRecordBox();

    Box<DownloadedArea> getDownloadedAreaBox();

}
