package com.ccaroni.kreasport.explore.di;

import android.content.Context;

import com.ccaroni.kreasport.utils.CredentialsManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Master on 24/02/2018.
 */
@Module
public class CredentialsModule {

    @Singleton
    @Provides
    public CredentialsManager providesCredentialsManager(Context context) {
        return new CredentialsManager(context);
    }

}
