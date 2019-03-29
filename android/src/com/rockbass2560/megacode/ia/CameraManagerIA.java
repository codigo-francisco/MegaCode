package com.rockbass2560.megacode.ia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import com.google.common.collect.Lists;
import com.rockbass2560.megacode.Claves;

import java.nio.ByteBuffer;
import java.util.List;

import androidx.annotation.NonNull;

public class CameraManagerIA {
    private static String TAG = CameraManagerIA.class.getName();
    private int format;
    private FaceRecognition faceRecognition;
    private ImageReader imageReader;
    private Handler handler;
    private HandlerThread handlerThread;
    private Size size;
    private Thread thread;
    private CameraCaptureSession cameraCaptureSession;
    private CameraManager cameraManager;
    private String idCamera;
    private CameraDevice cameraDevice;
    private static final String NAME_THREAD = "CameraManagerIA";
    private Handler.Callback handlerCallback;
    public boolean isRunning = false;

    public CameraManagerIA(Context context, Handler.Callback handlerCallback){
        try {
            faceRecognition = FaceRecognition.getInstance(context, FaceRecognition.HAAR_CASCADE_DEFAULT,
                    EmotionClassification.EMOTIV_INSIGHT_MODEL);

            this.handlerCallback = handlerCallback;

            cameraManager = context.getSystemService(CameraManager.class);
            idCamera = cameraManager.getCameraIdList()[1]; //La 1 es la frontal (casí siempre)

            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(idCamera);
            StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            format = getOutputFormatAvailable(map);

            size = chooseOutputSize(map.getOutputSizes(format));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private ImageReader.OnImageAvailableListener imageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            //Imagen
            Image image = reader.acquireLatestImage();
            if (format == ImageFormat.FLEX_RGBA_8888){
                Image.Plane[] planes = image.getPlanes();
                ByteBuffer r = planes[0].getBuffer();
                ByteBuffer g = planes[1].getBuffer();
                ByteBuffer b = planes[2].getBuffer();
                ByteBuffer a = planes[3].getBuffer();

                int [] pixels = new int[size.getHeight()*size.getWidth()];

                for (int index = 0; index < pixels.length; index++){
                    int color = Color.argb((int)a.get(),(int)r.get(),(int)g.get(),(int)b.get());
                    pixels[index] = color;
                }

                Bitmap bitmap = Bitmap.createBitmap(pixels,image.getWidth(), image.getHeight(),
                        Bitmap.Config.ARGB_8888);

                String emotion = faceRecognition.detectEmotion(bitmap);

                Log.d(TAG, emotion);
            }else if (image.getFormat() == ImageFormat.JPEG){
                ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
                byteBuffer.rewind();
                byte[] imageData = new byte[byteBuffer.remaining()];
                byteBuffer.get(imageData);

                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                String emotion = faceRecognition.detectEmotion(bitmap);

                if (!emotion.equals(FaceRecognition.NOT_FOUND)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Claves.EMOTION, emotion);

                    Message message = handler.obtainMessage(Claves.EMOTION_FOUND);
                    message.setData(bundle);

                    handler.sendMessage(message);
                }
            }
            image.close();
        }
    };

    private CameraDevice.StateCallback cameraCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            try {
                cameraDevice = camera;

                imageReader = ImageReader.newInstance(size.getWidth(),size.getHeight(), format, 1);
                imageReader.setOnImageAvailableListener(imageAvailableListener, handler);
                Surface imageSurface = imageReader.getSurface();

                CaptureRequest.Builder captureRequestBuild = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                captureRequestBuild.addTarget(imageSurface);
                //Range<Integer> fps = Range.create(1,1);
                //captureRequestBuild.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, fps);
                CaptureRequest request = captureRequestBuild.build();

                List<Surface> outputs = Lists.newArrayList(imageSurface);

                camera.createCaptureSession(outputs, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        try {
                            cameraCaptureSession = session;
                            session.setRepeatingRequest(request, null, handler);
                        } catch (CameraAccessException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                    }
                }, handler);

            } catch (CameraAccessException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
        }
    };

    private int getOutputFormatAvailable(StreamConfigurationMap map){
        format = ImageFormat.JPEG;

		/*if (map.isOutputSupportedFor(ImageFormat.FLEX_RGBA_8888)){
			format = ImageFormat.FLEX_RGBA_8888;
		}else if (map.isOutputSupportedFor(PixelFormat.RGBA_8888)){
			format = PixelFormat.RGBA_8888;
		}*/

        return format;
    }

    public Size chooseOutputSize(Size[] sizes){
        Size chooseSize = null;

        for (int index = sizes.length-1; index > 0; index--){
            Size size = sizes[index];
            if (size.getWidth() > 300 && size.getHeight()>300){
                chooseSize = size;
                break;
            }
        }

        return chooseSize;
    }

    public void iniciarCamara(){
        isRunning = true;
        try {
            if (handlerThread==null) {
                handlerThread = new HandlerThread(NAME_THREAD);
                handlerThread.start();
            }
            if (handler==null)
                handler = new Handler(handlerThread.getLooper(), handlerCallback);

            cameraManager.openCamera(idCamera, cameraCallBack, handler);
        } catch (CameraAccessException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void cerrarCamara(){
        isRunning = false;
        try {
            if (cameraCaptureSession!=null) {
                cameraCaptureSession.stopRepeating();
                cameraCaptureSession.close();
                cameraCaptureSession = null;
            }
            if (cameraDevice!=null) {
                cameraDevice.close();
                cameraDevice = null;
            }
            if (imageReader!=null) {
                imageReader.close();
                imageReader = null;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    public void cerrarTodo(){
        isRunning = false;
        try {

            handlerThread.quitSafely();
            handlerThread.join();
            handlerThread = null;
            handler = null;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
