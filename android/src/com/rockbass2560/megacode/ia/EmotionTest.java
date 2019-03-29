package com.rockbass2560.megacode.ia;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;
import java.io.InputStream;

public class EmotionTest {

    private final static String TAG = EmotionTest.class.getName();

    private FaceRecognition faceRecognition;
    private Context context;
    private final static String ruta = "E:\\RafD_Ordenado\\";

    public EmotionTest(FaceRecognition faceRecognition, Context context){
        this.faceRecognition = faceRecognition;
        this.context = context;
    }

    public void doTest(){
        int correctas = 0, incorrectas = 0, indiceEmocion = 0;
        AssetManager assetManager = context.getAssets();
        for (String carpeta : faceRecognition.emotionClassification.emotions){
            try {
                for (String foto : assetManager.list("subrafd/"+carpeta.toLowerCase())){
                    InputStream inputStream = assetManager.open("subrafd/"+carpeta.toLowerCase()+"/"+foto);
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    Mat img = org.opencv.imgcodecs.Imgcodecs.imdecode(new MatOfByte(bytes),Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
                    Object[] returnResult = faceRecognition.getFaceAndDetectEmotion(img);
                    boolean result = (boolean)returnResult[1];
                    String emocion = (String)returnResult[0];
                    if (emocion.equals(faceRecognition.emotionClassification.emotions[indiceEmocion])){
                        correctas++;
                    }else{
                        incorrectas++;
                    }
                }
                indiceEmocion++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "Correctas: " + correctas);
        Log.d(TAG, "Incorrectas: " + incorrectas);
    }

}
