package com.ccaroni.kreasport.race.dagger;

import android.app.Application;

import com.ccaroni.kreasport.data.local.Checkpoint;
import com.ccaroni.kreasport.data.local.DownloadedArea;
import com.ccaroni.kreasport.data.local.domain.MyObjectBox;
import com.ccaroni.kreasport.data.local.Race;
import com.ccaroni.kreasport.data.local.Record;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by Master on 17/02/2018.
 */
@Module
public class BoxStoreModule {

    private BoxStore mBoxStore;

    public BoxStoreModule(Application application) {
        this.mBoxStore = MyObjectBox.builder().androidContext(application).build();
    }

    @Singleton
    @Provides
    public BoxStore providesBoxStore() {
        return this.mBoxStore;
    }

    @Singleton
    @Provides
    public Box<Race> providesRaceBox(BoxStore boxStore) {
        return boxStore.boxFor(Race.class);
    }

    @Singleton
    @Provides
    public Box<Checkpoint> providesCheckpointBox(BoxStore boxStore) {
        return boxStore.boxFor(Checkpoint.class);
    }

    @Singleton
    @Provides
    public Box<Record> providesRecordBox(BoxStore boxStore) {
        return boxStore.boxFor(Record.class);
    }

    @Singleton
    @Provides
    public Box<DownloadedArea> providesDownloadedAreaBox(BoxStore boxStore) {
        return boxStore.boxFor(DownloadedArea.class);
    }
}
