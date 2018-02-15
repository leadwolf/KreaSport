package com.ccaroni.kreasport.data.local.dao;

import com.ccaroni.kreasport.data.local.domain.Checkpoint;
import com.ccaroni.kreasport.data.local.domain.DownloadedArea;
import com.ccaroni.kreasport.data.local.domain.Race;
import com.ccaroni.kreasport.data.local.domain.Record;
import com.ccaroni.kreasport.race.view.activity.App;

import io.objectbox.Box;

/**
 * Created by Master on 13/02/2018.
 */

public class BoxStoreManager {

    private Box<Race> raceBox;
    private Box<Checkpoint> checkpointBox;
    private Box<Record> recordBox;
    private Box<DownloadedArea> downloadedAreaBox;

    private static class Loader {
        static volatile BoxStoreManager INSTANCE = new BoxStoreManager();
    }

    private BoxStoreManager() {
        raceBox = App.get().getBoxStore().boxFor(Race.class);
        checkpointBox = App.get().getBoxStore().boxFor(Checkpoint.class);
        recordBox = App.get().getBoxStore().boxFor(Record.class);
        downloadedAreaBox = App.get().getBoxStore().boxFor(DownloadedArea.class);
    }

    public static BoxStoreManager getInstance() {
        return Loader.INSTANCE;
    }
}
