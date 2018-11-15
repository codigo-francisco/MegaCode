package com.megacode.views.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.megacode.GigaGalGame;
import com.megacode.R;
import com.megacode.others.CustomCallback;
import com.megacode.others.FaceRecognition;
import com.megacode.views.fragments.GameFragment;
import com.megacode.GameplayScreen;
import com.megacode.Level;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.Arrays;
import java.util.LinkedList;

public class MegaCodeAcitivity extends AppCompatActivity implements  AndroidFragmentApplication.Callbacks, CameraBridgeViewBase.CvCameraViewListener2 {

	private final static String TAG = "MegaCodeActivity";
	private CameraBridgeViewBase cameraBridgeViewBase;
	private FaceRecognition faceRecognition;
	private TextView textViewEmotion;
	private ImageView imageViewFace;
	private LinearLayout linearLayoutCamera;
	private WebView webView;
	private GameFragment libgdxFragment;

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode==0){
            if (grantResults.length>0){
                inicializarCamara();
            }
		}
	}

	private final static int idMenuCamera = 0;
	private final static int idRecargarBlockly = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, idMenuCamera, Menu.NONE, "Abrir/Cerrar Camara");
        menu.add(Menu.NONE, idRecargarBlockly, Menu.NONE, "Recargar Blockly");

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
        }else if (idItem==idRecargarBlockly){
        	webView.reload();
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

		Intent intent = getIntent();
		String rutaNivel = null;
		if (intent!=null){
			rutaNivel = intent.getStringExtra("rutaNivel");
		}

		if (rutaNivel==null){
			Toast.makeText(this, "No se ha cargado la ruta del nivel correctamente",Toast.LENGTH_LONG).show();
			finish();
		}

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

		webView = findViewById(R.id.megacode_activity_webview);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webView.addJavascriptInterface(new WebViewJavaScriptInterface(), "megacode");

		webView.loadUrl("file:///android_asset/blockly/index.html");

        findViewById(R.id.megacode_play).setOnClickListener(view -> webView.loadUrl("javascript:runBlockly()"));

        cargarJuego(rutaNivel);

	}

	private void cargarJuego(String rutaNivel){
		// Create libgdx fragment
		libgdxFragment = new GameFragment(rutaNivel);

		libgdxFragment.getGame().addLoadGameListener(new GigaGalGame.LoadGameListener() {
			@Override
			public void loadedGame() {
				libgdxFragment.getGamePlayScreen().addNivelCompletadoListener(new GameplayScreen.NivelCompletadoListener() {
					@Override
					public void nivelTerminado(GameplayScreen screen) {
						libgdxFragment.handler.post(new Runnable() {
							@Override
							public void run() {
								AlertDialog alertDialog = new AlertDialog.Builder(MegaCodeAcitivity.this)
										.setTitle("Continuar los ejercicios")
										.setMessage("¿Quieres pasar a otro nivel?")
										.setPositiveButton("Si", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialogInterface, int i) {
												screen.dispose();
												libgdxFragment.getGame().dispose();
												libgdxFragment = null;
												//Mejor solución pendiente
												cargarJuego("levels/nivel2.dt");

												//Obtener siguiente nivel, iniciarlo
												//screen.startNewLevel();
											}
										})
										.setNegativeButton("No", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialogInterface, int i) {
												MegaCodeAcitivity.this.finish();
											}
										})
										.setCancelable(false)
										.create();

								alertDialog.show();
							}
						});
					}
				});
			}
		});

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.game_fragment);

		if (fragment!=null)
			transaction.replace(R.id.game_fragment, libgdxFragment).commit();
		else
			transaction.add(R.id.game_fragment, libgdxFragment).commit();


	}

    class WebViewJavaScriptInterface{

        @JavascriptInterface
        public void runBlockly(String code){
        	GameplayScreen screen = libgdxFragment.getGamePlayScreen();

			Level level = screen.level;

            Log.d(TAG, code);

            String[] comandos = code.split(",");

            Log.d(TAG, "Procesando comandos: " + comandos);

            level.setComandos(new LinkedList<>(Arrays.asList(comandos)));
            level.procesarComandos();
        }
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
		//Core.flip(lastFrame, lastFrame,0);
		//Core.transpose(lastFrame, lastFrame);
		return  faceRecognition.markFace(lastFrame);
	}
}
