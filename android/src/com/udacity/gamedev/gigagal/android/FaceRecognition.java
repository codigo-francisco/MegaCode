package com.udacity.gamedev.gigagal.android;

import android.app.Activity;
import android.content.res.AssetManager;
import android.media.FaceDetector;

import org.opencv.objdetect.CascadeClassifier;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Francisco on 23/02/2018.
 */

public class FaceRecognition {

    private static FaceRecognition faceRecognition;
    private CascadeClassifier cascadeClassifier;

    private static FaceRecognition getFaceRecognition(Activity activity){
        if (faceRecognition==null){
            faceRecognition = new FaceRecognition(activity);
        }

        return faceRecognition;
    }

    public FaceRecognition(Activity manager){

        try {
            InputStream is = manager.open("lbpcascade_frontalface_improved.xml");
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            os.write(buffer);

            is.close();
            os.close();

            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            cascadeClassifier.load(mCascadeFile.getAbsolutePath());

            mCascadeFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
