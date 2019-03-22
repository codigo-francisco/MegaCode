package com.megacode.ia;

import android.content.Context;
import android.graphics.Bitmap;

import com.megacode.Claves;
import com.megacode.helpers.FileHelper;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.Interpreter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleToIntFunction;
import java.util.stream.Stream;

public class EmotionClassification {

    //Variables de configuración
    private final static String[] EMOTIONS_RAFD = new String[]{
            "ENOJADO",
            "FELIZ",
            "MIEDO",
            "NEUTRAL",
            "SORPRESA",
            "TRISTE"
    };

    public final static String RAFD_MODEL = "model.tflite";

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

        //4 Magico >=(
        imgData = ByteBuffer.allocateDirect(4*240*240*3);
        imgData.order(ByteOrder.nativeOrder());

        intValues = new int[240*240];

        selectEmotions(model);
    }

    private void selectEmotions(String model){
        switch (model){
            case RAFD_MODEL:
                emotions = EMOTIONS_RAFD;
                tamañoFoto = new Size(240, 240);
                break;
        }

        probsEmotions = new float[1][emotions.length];
    }

    private void convertMattoTfLiteInput(Mat mat)
    {
        imgData.rewind();
        double[] values;
        for (int i = 0; i < Claves.DIM_HEIGHT; ++i) {
            for (int j = 0; j < Claves.DIM_WIDTH; ++j) {
                values = mat.get(i,j);
                imgData.putFloat((float)values[0]);
                imgData.putFloat((float)values[1]);
                imgData.putFloat((float)values[2]);
            }
        }
    }

    public void convertBitmapToByteBuffer(Bitmap bitmap) {

        imgData.rewind();

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());
        // Encode the image pixels into a byte buffer representation matching the expected
        // input of the Tensorflow model
        int pixel = 0;
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                final int val = intValues[pixel++];
                imgData.putInt(((val >> 16) & 0xFF));
                imgData.putInt(((val >> 8) & 0xFF));
                imgData.putInt((val & 0xFF));
            }
        }
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

        return emotions[(int)argmax(probsEmotions[0])[0]];
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
