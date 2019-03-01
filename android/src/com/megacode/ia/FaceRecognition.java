package com.megacode.ia;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.megacode.Claves;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Francisco on 23/02/2018.
 */

public class FaceRecognition {
    private final static String TAG = FaceRecognition.class.getName();

    private CascadeClassifier cascadeClassifier;
    public EmotionClassification emotionClassification;
    private static final String url  = "http:/192.168.1.96/emocion";

    public static final String HAAR_CASCADE_DEFAULT = "haarcascade_frontalface_default.xml";
    public static final String LBP_CASCADE_IMPROVED = "lbpcascade_frontalface_improved.xml";
    public static final String LBP_CASCADE = "lbpcascade_frontalface.xml";

    public FaceRecognition(Context context, String cascade) throws IOException{
        createCascadeFile(context, cascade);
    }

    public FaceRecognition(Context context, String cascade, String model) throws IOException {
        createCascadeFile(context, cascade);
        emotionClassification = EmotionClassification.getEmotionClassification(model, context);
    }

    private void createCascadeFile(Context context, String cascade) throws IOException {
        InputStream is = context.getAssets().open(cascade);
        File mCascadeFile = File.createTempFile("cascade",".xml");
        FileOutputStream os = new FileOutputStream(mCascadeFile);

        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        os.write(buffer);

        is.close();
        os.close();

        //cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        cascadeClassifier = new CascadeClassifier();
        cascadeClassifier.load(mCascadeFile.getAbsolutePath());

        mCascadeFile.delete();
    }

    /*private String getEncodedString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();

        return  Base64.encodeToString(byteArray, Base64.DEFAULT);
    }*/

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
            Bitmap newImage = Bitmap.createBitmap(240, 240, Bitmap.Config.RGB_565);
            Utils.matToBitmap(image,newImage);
            emotion = emotionClassification.classify(newImage);

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
            org.opencv.imgproc.Imgproc.resize(newFace, newFace, new Size(Claves.DIM_WIDTH, Claves.DIM_HEIGHT));

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
