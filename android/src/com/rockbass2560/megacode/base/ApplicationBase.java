package com.rockbass2560.megacode.base;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.webkit.WebView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.rockbass2560.megacode.helpers.HtmlHelper;
import com.rockbass2560.megacode.ia.EmotionClassification;
import com.rockbass2560.megacode.ia.EmotionTest;
import com.rockbass2560.megacode.ia.FaceRecognition;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ApplicationBase extends Application {

    private final static String TAG = ApplicationBase.class.getName();

    private Timer duracionApp;
    private long duracion;

    @Override
    public void onCreate() {
        super.onCreate();

        duracionApp = new Timer("duracionApp");
        duracionApp.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                duracion++;
            }
        }, 0, 1000);

        /*if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);*/

        if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        HtmlHelper.theme = new Theme(new AndroidTemplates(this));

        OpenCVLoader.initDebug();

        /*try {

            //FaceRecognition faceRecognition = new FaceRecognition(this, FaceRecognition.HAAR_CASCADE_DEFAULT, EmotionClassification.RAFD_MODEL);
            //EmotionTest test = new EmotionTest(faceRecognition, this);
            //test.doTest();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }*/
    }
}
