package com.ccaroni.kreasport.explore.view.activity;

import android.app.Application;

import com.ccaroni.kreasport.explore.di.BoxComponent;
import com.ccaroni.kreasport.explore.di.BoxStoreModule;
import com.ccaroni.kreasport.explore.di.DaggerBoxComponent;
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
