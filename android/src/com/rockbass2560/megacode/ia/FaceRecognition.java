package com.rockbass2560.megacode.ia;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.google.common.io.Files;
import com.google.firebase.storage.FirebaseStorage;
import com.rockbass2560.megacode.Claves;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.util.Random;

/**
 * Created by Francisco on 23/02/2018.
 */

public class FaceRecognition {
    private final static String TAG = FaceRecognition.class.getName();

    private CascadeClassifier cascadeClassifier;
    public EmotionClassification emotionClassification;

    public static final String HAAR_CASCADE_DEFAULT = "haarcascade_frontalface_default";
    public static final String LBP_CASCADE_IMPROVED = "lbpcascade_frontalface_improved";
    public static final String LBP_CASCADE = "lbpcascade_frontalface";

    public static final String NOT_FOUND = "NoEncontrado";

    private File externalFolder;

    private static FaceRecognition SINGLETON;
    private Context context;

    public static FaceRecognition getInstance(Context context, String cascade, String model) throws IOException {
        if (SINGLETON==null){
            SINGLETON = new FaceRecognition(context, cascade, model);
        }

        return SINGLETON;
    }

    private FaceRecognition(Context context, String cascade, String model) throws IOException {
        createCascadeFile(context, cascade);
        emotionClassification = EmotionClassification.getEmotionClassification(model, context);
        externalFolder = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        this.context = context;
    }

    private void createCascadeFile(Context context, String cascade) throws IOException {
        String nameFile = cascade+".xml";
        File cascadeFile = new File(context.getFilesDir(), nameFile);
        if (!cascadeFile.exists()){
            InputStream is = context.getAssets().open(nameFile);
            cascadeFile.createNewFile();

            FileOutputStream os = new FileOutputStream(cascadeFile);
            byte[] buffer = new byte[is.available()];

            is.read(buffer);
            os.write(buffer);

            is.close();
            os.close();
        }

        cascadeClassifier = new CascadeClassifier();
        cascadeClassifier.load(cascadeFile.getAbsolutePath());
    }

    public String detectEmotion(Bitmap bitmap){
        String result = NOT_FOUND;

        Mat imagen = new Mat();

        Utils.bitmapToMat(bitmap, imagen);

        MatOfRect matOfRect = new MatOfRect();

        cascadeClassifier.detectMultiScale(imagen, matOfRect);

        if (!matOfRect.empty()){
            Rect rect = matOfRect.toList().get(0);
            imagen = new Mat(imagen, rect);
            org.opencv.imgproc.Imgproc.cvtColor(imagen, imagen, Imgproc.COLOR_RGBA2GRAY);
            org.opencv.imgproc.Imgproc.resize(imagen, imagen, emotionClassification.tamañoFoto);

            /*Bitmap faceImage = Bitmap.createBitmap(imagen.cols(), imagen.rows(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(imagen, faceImage);
            String title = new Random().nextInt(Integer.MAX_VALUE)+"";
            MediaStore.Images.Media.insertImage(context.getContentResolver(), faceImage, title, title);*/

            result = emotionClassification.classify(imagen);
        }

        imagen.release();

        return result;
    }

    /**
     * Obtiene el rostro y procesa la emoción
     * @param image imagen en formato OpenCV
     * @return un arreglo donde la primera posición es la emoción y la segunda una bandera que indica que se obtuvo el rostro
     */
    public Object[] getFaceAndDetectEmotion(Mat image){
        MatOfRect matOfRect = new MatOfRect();

        boolean result = false;
        String emotion = "";
        cascadeClassifier.detectMultiScale(image, matOfRect);

        if (!matOfRect.empty()){
            Rect rect = matOfRect.toArray()[0];
            image = new Mat(image, rect);
            org.opencv.imgproc.Imgproc.resize(image, image, new Size(emotionClassification.tamañoFoto.width, emotionClassification.tamañoFoto.height));
            //Bitmap newImage = Bitmap.createBitmap(240, 240, Bitmap.Config.RGB_565);
            //Utils.matToBitmap(image,newImage);
            emotion = emotionClassification.classify(image);

            result = true;
        }

        return new Object[]{emotion, result};
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

    public Mat markAndDetectEmotion(Mat image, boolean mirror){
        if (mirror){
            org.opencv.core.Core.flip(image, image, 1);
        }

        MatOfRect matOfRect = new MatOfRect();

        cascadeClassifier.detectMultiScale(image, matOfRect);

        if (!matOfRect.empty()) {
            Rect rect = matOfRect.toArray()[0];

            Mat newFace = new Mat(image, rect);
            org.opencv.imgproc.Imgproc.resize(newFace, newFace, emotionClassification.tamañoFoto);

            String emotion = emotionClassification.classify(newFace);

            newFace.release();

            Log.i(TAG, emotion);

            org.opencv.imgproc.Imgproc.putText(image, emotion, new Point(0, 0), Core.FONT_HERSHEY_COMPLEX, 2, new Scalar(125, 125, 125));

            org.opencv.imgproc.Imgproc.
                    rectangle(image, new Point(rect.x, rect.y),
                            new Point(rect.x + rect.width, rect.y + rect.height),
                            new Scalar(1, 1, 1), 5);
        }

        return image;
    }

}
