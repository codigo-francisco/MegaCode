package com.megacode.base;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import com.megacode.helpers.HtmlHelper;
import com.squareup.leakcanary.LeakCanary;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;

import org.opencv.android.OpenCVLoader;

public class ApplicationBase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);

        if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        HtmlHelper.theme = new Theme(new AndroidTemplates(this));
    }
}
