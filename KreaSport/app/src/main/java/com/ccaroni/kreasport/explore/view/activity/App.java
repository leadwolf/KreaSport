package com.ccaroni.kreasport.explore.view.activity;

import android.app.Application;

import com.ccaroni.kreasport.explore.dagger.BoxComponent;
import com.ccaroni.kreasport.explore.dagger.BoxStoreModule;
import com.ccaroni.kreasport.explore.dagger.DaggerBoxComponent;
import com.jakewharton.threetenabp.AndroidThreeTen;

/**
 * Created by Master on 17/02/2018.
 */

public class App extends Application {

    private static BoxComponent boxComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);

        boxComponent = DaggerBoxComponent.builder()
                .boxStoreModule(new BoxStoreModule(this))
                .build();
    }

    public static BoxComponent getBoxComponent() {
        return boxComponent;
    }
}
