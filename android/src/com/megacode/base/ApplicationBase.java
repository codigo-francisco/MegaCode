package com.megacode.base;

import android.app.Application;
import android.util.Log;

import org.opencv.android.OpenCVLoader;

import io.realm.Realm;

public class ApplicationBase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
