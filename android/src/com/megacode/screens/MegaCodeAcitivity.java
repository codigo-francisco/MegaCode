package com.megacode.screens;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.megacode.others.CustomCallback;
import com.megacode.others.FaceRecognition;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.Locale;

public class MegaCodeAcitivity extends AppCompatActivity implements  AndroidFragmentApplication.Callbacks, CameraBridgeViewBase.CvCameraViewListener2 {

	private final static String TAG = "Launcher";
	private CameraBridgeViewBase cameraBridgeViewBase;
	private FaceRecognition faceRecognition;
	private TextView textViewEmotion;
	private ImageView imageViewFace;
	private LinearLayout linearLayoutCamera;

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode==0){

		}
	}

	public LinearLayout getLinearLayoutCamera(){
		return linearLayoutCamera;
	}
	public CameraBridgeViewBase getCameraBridgeViewBase() { return cameraBridgeViewBase; }
	private int selectedFragment;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (getIntent()!=null && getIntent().getExtras()!=null){
		    selectedFragment = getIntent().getIntExtra("selectedFragment",R.id.feed);
        }else{
		    selectedFragment = R.id.feed;
        }

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA }, 0);
		}

		cameraBridgeViewBase = findViewById(R.id.camera_view);
		cameraBridgeViewBase.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
		cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
		cameraBridgeViewBase.setCvCameraViewListener(this);

		textViewEmotion = findViewById(R.id.text_view_emotion);
		imageViewFace = findViewById(R.id.image_view_face);
		linearLayoutCamera = findViewById(R.id.linear_layout_camera);

		Button butonCamera = findViewById(R.id.button_camera);
		butonCamera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bitmap imageFace = Bitmap.createBitmap(lastFrame.width(), lastFrame.height(), Bitmap.Config.ARGB_8888);
				Utils.matToBitmap(lastFrame, imageFace);
				imageViewFace.setImageBitmap(imageFace);
				textViewEmotion.setText("Detectando emocion...");
				faceRecognition.detectEmotion(lastFrame, new CustomCallback<String>() {
					@Override
					public void processResponse(String response) {
						textViewEmotion.setText(response);
					}
				});
			}
		});

		BlocklyFragment blocklyFragment = new BlocklyFragment();

		// Create libgdx fragment
		GameFragment libgdxFragment = new GameFragment();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		transaction
				.add(R.id.content_blockly, blocklyFragment)
				.add(R.id.content_framelayout, libgdxFragment)
				.commit();

	}

	@Override
	public void exit() {
		Log.d(TAG, "exit");
	}

	@Override
	public void onCameraViewStarted(int i, int i1) {

	}

	@Override
	public void onCameraViewStopped() {

	}

	protected Mat lastFrame;

	@Override
	public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame cvCameraViewFrame) {
		lastFrame = cvCameraViewFrame.rgba();
		return  faceRecognition.markFace(lastFrame);
	}

	BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
				case LoaderCallbackInterface.SUCCESS:
					cameraBridgeViewBase.enableView();
					faceRecognition = new FaceRecognition(getApplicationContext());
					break;
				default:
					super.onManagerConnected(status);
					break;
			}
		}
	};

	private static boolean initOpencv(){
		boolean result = false;
		try{
			System.loadLibrary("opencv_java");

			result = true;
		}catch(UnsatisfiedLinkError ex){
			Log.e(TAG, ex.getMessage(), ex);
		}

		return result;
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!initOpencv()){
			//Log.d(TAG, "opencv inicializado asincronicamente");
			//Este metodo dejo de funcionar en las versiones nuevas de Android, se realiza una instalación propia
			//OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseLoaderCallback);

			//Verificamos las arquitecturas disponibles, dependiendo de la arquitectura se descarga el so correcto
			//installOpenCVManager();
		}else{
			Log.d(TAG, "opencv inicializado");
		}
	}

    @Override
    protected void onPause() {
        Intent intent = new Intent();
        intent.putExtra("selectedFragment", selectedFragment);
        setResult(RESULT_OK, intent);
        super.onPause();
    }
}
