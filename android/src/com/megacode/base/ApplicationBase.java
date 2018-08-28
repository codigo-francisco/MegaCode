package com.megacode.base;

import android.app.Application;

import io.realm.Realm;

public class ApplicationBase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}