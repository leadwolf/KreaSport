package com.ccaroni.kreasport.race.dagger;

import com.ccaroni.kreasport.data.local.Checkpoint;
import com.ccaroni.kreasport.data.local.DownloadedArea;
import com.ccaroni.kreasport.data.local.Race;
import com.ccaroni.kreasport.data.local.Record;
import com.ccaroni.kreasport.race.view.fragments.ExploreFragment;

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

    Box<Race> getRaceBox();

    Box<Checkpoint> getCheckpointBox();

    Box<Record> getRecordBox();

    Box<DownloadedArea> getDownloadedAreaBox();

}
