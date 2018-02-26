package com.udacity.gamedev.gigagal.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Francisco on 23/02/2018.
 */

public class FaceRecognition {
    private final static String TAG = "FaceRecognition";

    private static FaceRecognition faceRecognition;
    private CascadeClassifier cascadeClassifier;
    private RequestQueue queue;
    private static final String url  = "http:/192.168.1.96:5000/emocion";

    public String detectEmotion(Mat image){
        final String default_result = "no_detectado";
        String result = default_result;
        Mat newFace = null;
        MatOfRect matOfRect = new MatOfRect();

        cascadeClassifier.detectMultiScale(image, matOfRect);

        if (!matOfRect.empty()){
            newFace = new Mat(image, matOfRect.toArray()[0]);
            org.opencv.imgproc.Imgproc.resize(newFace, newFace, new Size(150, 150));

            Bitmap bitMapFace = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);

            //Conversion de imagen a base64
            Utils.matToBitmap(newFace, bitMapFace);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitMapFace.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            String encodeString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Map<String, String> params = new HashMap<>();
            params.put("fotografia",encodeString);

            StringRequest stringRequest = new CustomRequest( Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Request de rostro recibido");
                    Log.d(TAG, "Emocion " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Request de rostro con error " +error.getMessage());
                }
            }, params );

            queue.add(stringRequest);
            queue.start();
        }

        return result;
    }

    public Mat getFace(Mat image){
        Mat newFace = null;
        MatOfRect matOfRect = new MatOfRect();

        cascadeClassifier.detectMultiScale(image, matOfRect);

        if (!matOfRect.empty()){
            newFace = new Mat(image, matOfRect.toArray()[0]);
            org.opencv.imgproc.Imgproc.resize(newFace, newFace, new Size(150, 150));
        }

        return newFace;
    }

    public Mat markFace(Mat image){
        MatOfRect matOfRect = new MatOfRect();

        cascadeClassifier.detectMultiScale(image, matOfRect);

        if (!matOfRect.empty()){
            Rect rect = matOfRect.toArray()[0];
            org.opencv.imgproc.Imgproc.
                    rectangle(image, new Point(rect.x, rect.y),
                            new Point(rect.x+rect.width, rect.y + rect.height),
                            new Scalar(1,1,1),5);
        }

        return image;
    }

    public FaceRecognition(Context context){

        queue = Volley.newRequestQueue(context);

        try {
            InputStream is = context.getAssets().open("lbpcascade_frontalface_improved.xml");
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
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
