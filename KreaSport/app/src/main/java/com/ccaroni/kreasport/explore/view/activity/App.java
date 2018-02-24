package com.ccaroni.kreasport.explore.view.activity;

import android.app.Application;

import com.ccaroni.kreasport.explore.di.AppComponent;
import com.ccaroni.kreasport.explore.di.AppModule;
import com.ccaroni.kreasport.explore.di.BoxStoreModule;
import com.ccaroni.kreasport.explore.di.CredentialsModule;
import com.ccaroni.kreasport.explore.di.DaggerAppComponent;
import com.ccaroni.kreasport.explore.di.ExploreComponent;
import com.ccaroni.kreasport.explore.di.ExploreModule;
import com.jakewharton.threetenabp.AndroidThreeTen;

/**
 * Created by Master on 17/02/2018.
 */

public class App extends Application {

    private static App instance;
    private AppComponent appComponent;
    private ExploreComponent exploreComponent;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);
        instance = this;
        this.appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .boxStoreModule(new BoxStoreModule(this))
                .credentialsModule(new CredentialsModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public ExploreComponent plusExploreComponent() {
        if (this.exploreComponent == null) {
            this.exploreComponent = this.appComponent.plusExploreComponent(new ExploreModule());
        }
        return this.exploreComponent;
    }

    public void clearExploreComponent() {
        this.exploreComponent = null;
    }
}
