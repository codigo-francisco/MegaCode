package com.rockbass2560.megacode.ia;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.rockbass2560.megacode.Claves;
import com.rockbass2560.megacode.helpers.FileHelper;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.tensorflow.lite.Interpreter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class EmotionClassification {

    private static final String TAG = EmotionClassification.class.getName();

    //Variables de configuración
    private final static String[] EMOTIONS_RAFD = new String[]{
            "ENOJADO",
            "FELIZ",
            "MIEDO",
            "NEUTRAL",
            "SORPRESA",
            "TRISTE"
    };

    private final static String[] EMOTIONS_EMOTIV_INSIGHT = new String[]{
            "bored",
            "engaged",
            "excited",
            "focused",
            "interested",
            "neutral"
    };

    private boolean color;

    public final static String RAFD_MODEL = "model.tflite";
    public final static String EMOTIV_INSIGHT_MODEL = "CNN_AG.tflite";
    private final static int TAMAÑO_DATA = 4;

    private static EmotionClassification emotionClassification;
    public String[] emotions;
    private float[][] probsEmotions;
    public org.opencv.core.Size tamañoFoto;
    private ByteBuffer imgData;
    private int[] intValues;

    public static EmotionClassification getEmotionClassification(String model, Context context){
        if (emotionClassification == null){
            emotionClassification = new EmotionClassification(model, context);
        }

        return emotionClassification;
    }

    private Interpreter tflite;

    private EmotionClassification(String model, Context context){
        tflite = new Interpreter(FileHelper.assetToByteBuffer(model, context));
        selectEmotions(model);

        int tamañoBuffer=0;
        if (color)
            tamañoBuffer = TAMAÑO_DATA * (int)tamañoFoto.width * (int)tamañoFoto.height * 3;
        else
            tamañoBuffer = TAMAÑO_DATA * (int)tamañoFoto.width * (int)tamañoFoto.height;

        imgData = ByteBuffer.allocateDirect(tamañoBuffer);
        imgData.order(ByteOrder.nativeOrder());

        intValues = new int[(int)tamañoFoto.width * (int)tamañoFoto.height];
    }

    private void selectEmotions(String model){
        switch (model){
            case RAFD_MODEL:
                emotions = EMOTIONS_RAFD;
                tamañoFoto = new Size(240, 240);
                color = true;
                break;
            case EMOTIV_INSIGHT_MODEL:
                emotions = EMOTIONS_EMOTIV_INSIGHT;
                tamañoFoto = new Size(150, 150);
                color = false;
                break;
        }

        probsEmotions = new float[1][emotions.length];
    }

    private void convertMattoTfLiteInput(Mat mat)
    {
        imgData.rewind();
        double[] values;
        for (int i = 0; i < tamañoFoto.height; ++i) {
            for (int j = 0; j < tamañoFoto.width; ++j) {
                values = mat.get(i,j);
                if (color){
                    imgData.putFloat((float)values[0]);
                    imgData.putFloat((float)values[1]);
                    imgData.putFloat((float)values[2]);
                }else{
                    imgData.putFloat((float)values[0]);
                }
            }
        }
    }

    public void convertBitmapToByteBuffer(Bitmap bitmap) {

    }

    public String classify(Bitmap image){
        convertBitmapToByteBuffer(image);

        return classify();
    }

    public String classify(Mat image){
        convertMattoTfLiteInput(image);

        return classify();
    }

    private String classify(){
        Arrays.fill(probsEmotions[0], 0);
        tflite.run(imgData, probsEmotions);

        float[] probs = probsEmotions[0];

        return emotions[(int)argmax(probs)[0]];
    }

    private Object[] argmax(float[] array){
        int best = -1;
        float best_confidence = 0.0f;

        for(int i = 0;i < array.length;i++){

            float value = array[i];

            if (value > best_confidence){

                best_confidence = value;
                best = i;
            }
        }

        return new Object[]{best,best_confidence};
    }
}
