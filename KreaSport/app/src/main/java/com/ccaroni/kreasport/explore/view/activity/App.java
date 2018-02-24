package com.ccaroni.kreasport.explore.view.activity;

import android.app.Application;

import com.ccaroni.kreasport.explore.di.BoxComponent;
import com.ccaroni.kreasport.explore.di.BoxStoreModule;
import com.ccaroni.kreasport.explore.di.DaggerBoxComponent;
import com.ccaroni.kreasport.explore.di.ExploreComponent;
import com.ccaroni.kreasport.explore.di.ExploreModule;
import com.jakewharton.threetenabp.AndroidThreeTen;

/**
 * Created by Master on 17/02/2018.
 */

public class App extends Application {

    private static App instance;
    private BoxComponent boxComponent;
    private ExploreComponent exploreComponent;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);
        instance = this;
        this.boxComponent = DaggerBoxComponent.builder()
                .boxStoreModule(new BoxStoreModule(this))
                .build();
    }

    public BoxComponent getBoxComponent() {
        return boxComponent;
    }

    public ExploreComponent plusExploreComponent() {
        if (this.exploreComponent == null) {
            this.exploreComponent = this.boxComponent.plusExploreComponent(new ExploreModule());
        }
        return this.exploreComponent;
    }

    public void clearExploreComponent() {
        this.exploreComponent = null;
    }
}
