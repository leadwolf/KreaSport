package com.ccaroni.kreasport.explore.di;

import com.ccaroni.kreasport.data.local.Checkpoint;
import com.ccaroni.kreasport.data.local.DownloadedArea;
import com.ccaroni.kreasport.data.local.Race;
import com.ccaroni.kreasport.data.local.Record;
import com.ccaroni.kreasport.explore.model.IRaceModel;
import com.ccaroni.kreasport.explore.model.impl.RaceModel;
import com.ccaroni.kreasport.explore.view.fragments.ExploreFragment;
import com.ccaroni.kreasport.explore.vm.AbstractExploreVM;
import com.ccaroni.kreasport.explore.vm.impl.ExploreVM;

import javax.inject.Singleton;

import dagger.Component;
import io.objectbox.Box;

/**
 * Created by Master on 17/02/2018.
 */

@Singleton
@Component(modules = {AppModule.class, BoxStoreModule.class})
public interface BoxComponent {

    ExploreComponent plusExploreComponent(ExploreModule exploreModule);

    Box<Race> getRaceBox();

    Box<Checkpoint> getCheckpointBox();

    Box<Record> getRecordBox();

    Box<DownloadedArea> getDownloadedAreaBox();

}
