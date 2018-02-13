package com.ccaroni.kreasport.race.view.activity;

import android.app.Application;

import com.ccaroni.kreasport.data.domain.MyObjectBox;

import io.objectbox.BoxStore;

/**
 * Created by Master on 13/02/2018.
 */

public class App extends Application {

    private static App instance;

    private BoxStore boxStore;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        boxStore = MyObjectBox.builder().androidContext(App.this).build();
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}
