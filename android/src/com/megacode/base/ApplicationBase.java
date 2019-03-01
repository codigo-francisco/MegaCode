package com.megacode.base;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import com.megacode.helpers.HtmlHelper;
import com.megacode.ia.EmotionClassification;
import com.megacode.ia.EmotionTest;
import com.megacode.ia.FaceRecognition;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class ApplicationBase extends Application {

    private final static String TAG = ApplicationBase.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();

        /*if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);*/

        if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        HtmlHelper.theme = new Theme(new AndroidTemplates(this));

        try {
            OpenCVLoader.initDebug();
            FaceRecognition faceRecognition = new FaceRecognition(this, FaceRecognition.HAAR_CASCADE_DEFAULT, EmotionClassification.RAFD_MODEL);
            EmotionTest test = new EmotionTest(faceRecognition, this);
            test.doTest();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
