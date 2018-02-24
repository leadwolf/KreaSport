package com.ccaroni.kreasport.explore.di;

import com.ccaroni.kreasport.data.local.Checkpoint;
import com.ccaroni.kreasport.data.local.DownloadedArea;
import com.ccaroni.kreasport.data.local.Race;
import com.ccaroni.kreasport.data.local.Record;
import com.ccaroni.kreasport.legacy.view.activities.entry.LoginActivity;
import com.ccaroni.kreasport.utils.CredentialsManager;

import javax.inject.Singleton;

import dagger.Component;
import io.objectbox.Box;

/**
 * Created by Master on 17/02/2018.
 */

@Singleton
@Component(modules = {AppModule.class, BoxStoreModule.class, CredentialsModule.class})
public interface AppComponent {

    ExploreComponent plusExploreComponent(ExploreModule exploreModule);

    Box<Race> getRaceBox();

    Box<Checkpoint> getCheckpointBox();

    Box<Record> getRecordBox();

    Box<DownloadedArea> getDownloadedAreaBox();

    CredentialsManager getCredentialsManager();

    void inject(LoginActivity loginActivity);
}
