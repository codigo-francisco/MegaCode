package com.udacity.gamedev.gigagal.android;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Francisco on 23/02/2018.
 */

public class FaceRecognition {
    private final static String TAG = "FaceRecognition";

    private static FaceRecognition faceRecognition;
    private CascadeClassifier cascadeClassifier;

    private static FaceRecognition getFaceRecognition(Activity activity){
        if (faceRecognition==null){
            faceRecognition = new FaceRecognition(activity);
        }

        return faceRecognition;
    }

    public boolean getFace(Mat image, Mat face){
        boolean result = false;
        MatOfRect matOfRect = new MatOfRect();

        cascadeClassifier.detectMultiScale(image, matOfRect);

        if (!matOfRect.empty()){
            face = new Mat(image, matOfRect.toArray()[0]);
            org.opencv.imgproc.Imgproc.resize(face, face, new Size(150, 150));
            result = true;
        }

        return result;
    }

    public FaceRecognition(Activity activity){

        try {
            InputStream is = activity.getAssets().open("lbpcascade_frontalface_improved.xml");
            File cascadeDir = activity.getDir("cascade", Context.MODE_PRIVATE);
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
            Log.e(TAG, "Error face recognition: "+e.getMessage());
        }

    }

}
