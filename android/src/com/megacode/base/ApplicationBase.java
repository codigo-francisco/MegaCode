package com.megacode.base;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import org.opencv.android.OpenCVLoader;

public class ApplicationBase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }
}
