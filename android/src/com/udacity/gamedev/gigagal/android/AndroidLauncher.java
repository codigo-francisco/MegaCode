package com.udacity.gamedev.gigagal.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class AndroidLauncher extends AppCompatActivity implements  AndroidFragmentApplication.Callbacks, CameraBridgeViewBase.CvCameraViewListener2 {

	private final static String TAG = "Launcher";
	private CameraBridgeViewBase cameraBridgeViewBase;

	BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
				case LoaderCallbackInterface.SUCCESS:
					cameraBridgeViewBase.enableView();
					break;
				default:
					super.onManagerConnected(status);
					break;
			}
		}
	};

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
		}else{
			exit();
		}

		cameraBridgeViewBase = (CameraBridgeViewBase)findViewById(R.id.camera_view);
		cameraBridgeViewBase.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
		cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
		cameraBridgeViewBase.setCvCameraViewListener(this);
		cameraBridgeViewBase.setClickable(true);
		cameraBridgeViewBase.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				Log.d(TAG, "click en camara");

				return false;
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

	private Mat lastFrame;

	@Override
	public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame cvCameraViewFrame) {
		lastFrame = cvCameraViewFrame.gray();
		return cvCameraViewFrame.rgba();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!OpenCVLoader.initDebug()){
			Log.d(TAG, "opencv inicializado");
			OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, baseLoaderCallback);
		}else{
			Log.d(TAG, "opencv no inicializado");
		}
	}
}
