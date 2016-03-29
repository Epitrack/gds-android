package com.epitrack.guardioes.manager;

import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * @author Igor Morais
 */
public final class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(getBaseContext());
        Fabric.with(this, new Crashlytics());
    }
}
