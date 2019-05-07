package com.rockbass2560.megacode.base;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.webkit.WebView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.rockbass2560.megacode.Claves;
import com.rockbass2560.megacode.helpers.HtmlHelper;
import com.rockbass2560.megacode.ia.EmotionClassification;
import com.rockbass2560.megacode.ia.EmotionTest;
import com.rockbass2560.megacode.ia.FaceRecognition;
import com.rockbass2560.megacode.ia.FuzzyLogic;
import com.rockbass2560.megacode.models.database.Conexion;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class ApplicationBase extends Application implements LifecycleObserver {

    private final static String TAG = ApplicationBase.class.getName();

    private Timer duracionApp;
    private TimerTask runnableTask;
    private long duracion;
    private long desconexiones;
    private long conexiones;

    @Override
    public void onCreate() {
        super.onCreate();

        duracionApp = new Timer("duracionApp");

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        /*if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);*/

        if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        HtmlHelper.theme = new Theme(new AndroidTemplates(this));

        OpenCVLoader.initDebug();

        FuzzyLogic.init();

        /*try {

            //FaceRecognition faceRecognition = new FaceRecognition(this, FaceRecognition.HAAR_CASCADE_DEFAULT, EmotionClassification.RAFD_MODEL);
            //EmotionTest test = new EmotionTest(faceRecognition, this);
            //test.doTest();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }*/
    }

    private TimerTask createTimerTask(){
        return new TimerTask() {
            @Override
            public void run() {
                duracion++;
            }
        };
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void appResume(){
        conexiones++;
        runnableTask = createTimerTask();
        duracionApp.scheduleAtFixedRate(runnableTask, 0, 1000);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void appStop(){
        //Guardar duración de la conexión
        runnableTask.cancel();
        desconexiones++;
        final SharedPreferences sharedPreferences = getSharedPreferences(Claves.SHARED_MEGACODE_PREFERENCES, MODE_PRIVATE);
        final String conexionId = sharedPreferences.getString(Claves.CONEXION_ID, Claves.EMPTY_STRING);

        if (!conexionId.isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference conexionDocument = db.document("Conexiones/" + conexionId);

            conexionDocument.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Conexion conexion = documentSnapshot.toObject(Conexion.class);
                            conexion.duracion = duracion;
                            conexion.salida = Timestamp.now();
                            conexion.desconexiones = desconexiones;
                            conexion.conexiones = conexiones;

                            conexionDocument.set(conexion);
                        }
                    });
        }
    }
}
