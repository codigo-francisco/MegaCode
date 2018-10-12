package com.megacode.screens;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.megacode.others.CustomCallback;
import com.megacode.others.FaceRecognition;
import com.udacity.gamedev.gigagal.GameplayScreen;
import com.udacity.gamedev.gigagal.Level;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.Arrays;
import java.util.LinkedList;

public class MegaCodeAcitivity extends ActivityBase implements  AndroidFragmentApplication.Callbacks, CameraBridgeViewBase.CvCameraViewListener2 {

	private final static String TAG = "Launcher";
	private CameraBridgeViewBase cameraBridgeViewBase;
	private FaceRecognition faceRecognition;
	private TextView textViewEmotion;
	private ImageView imageViewFace;
	private LinearLayout linearLayoutCamera;
	private Level level;

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode==0){
			if (grantResults.length>0){
				inicializarCamara();
			}
		}
	}

	private int idMenuCamera;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		idMenuCamera = menu.add("Mostrar/Ocultar Camara").getItemId();

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int idItem = item.getItemId();

		if (idItem==idMenuCamera){
			if (linearLayoutCamera.getVisibility() == View.GONE) {
				linearLayoutCamera.setVisibility(View.VISIBLE);
				//camera.enableView();
				cameraBridgeViewBase.setVisibility(View.VISIBLE);
			}
			else {
				//camera.disableView();
				cameraBridgeViewBase.setVisibility(View.GONE);
				linearLayoutCamera.setVisibility(View.GONE);
			}
		}

		return super.onOptionsItemSelected(item);
	}

	public LinearLayout getLinearLayoutCamera(){
		return linearLayoutCamera;
	}

	private void inicializarCamara(){
		OpenCVLoader.initDebug();

		cameraBridgeViewBase.enableView();
		faceRecognition = new FaceRecognition(getApplicationContext());
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cameraBridgeViewBase = findViewById(R.id.camera_view);
		cameraBridgeViewBase.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
		cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
		cameraBridgeViewBase.setCvCameraViewListener(this);

		textViewEmotion = findViewById(R.id.text_view_emotion);
		imageViewFace = findViewById(R.id.image_view_face);
		linearLayoutCamera = findViewById(R.id.linear_layout_camera);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA }, 0);
		}else{
			inicializarCamara();
		}

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

		FloatingActionButton floatingActionButton = findViewById(R.id.megacode_play);
		floatingActionButton.setOnClickListener(view -> {
			blocklyFragment.runCode();
		});

		blocklyFragment.setCodeGeneratorCallback(generatedCode -> {
			if (level == null) {
				level = ((GameplayScreen)GameFragment.GAME.getScreen()).level;
			}

			Log.d(TAG, generatedCode);

			String[] comandos = generatedCode.split(",");

			Log.d(TAG, "Procesando comandos: " + comandos);

			level.setComandos(new LinkedList<>(Arrays.asList(comandos)));
			level.procesarComandos();
		});


		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		transaction.add(R.id.content_blockly, blocklyFragment)
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
}
