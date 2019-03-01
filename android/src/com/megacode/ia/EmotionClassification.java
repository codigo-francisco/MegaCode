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
import java.util.Arrays;
import java.util.List;

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
                imgData.putFloat(((val >> 16) & 0xFF));
                imgData.putFloat(((val >> 8) & 0xFF));
                imgData.putFloat((val & 0xFF));
            }
        }
    }

    public String classify(Bitmap image){
        convertBitmapToByteBuffer(image);

        return classify(imgData);
    }

    public String classify(Mat image){
        ByteBuffer imageBuffer = convertMattoTfLiteInput(image);

        return classify(imageBuffer);
    }

    public String classify(ByteBuffer image){
        Arrays.fill(probsEmotions[0], 0);
        tflite.run(image, probsEmotions);

        return emotions[(int)argmax(probsEmotions[0])[0]];
    }

    private ByteBuffer convertMattoTfLiteInput( Mat mat)
    {
        ByteBuffer imgData = ByteBuffer.allocateDirect(Claves.TAMAÑO_BUFFER_FOTO);
        imgData.order(ByteOrder.nativeOrder());
        imgData.rewind();
        //int pixel = 0;
        for (int i = 0; i < Claves.DIM_HEIGHT; ++i) {
            for (int j = 0; j < Claves.DIM_WIDTH; ++j) {
                imgData.putFloat((float)mat.get(i,j)[0]);
            }
        }

        return imgData;
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
