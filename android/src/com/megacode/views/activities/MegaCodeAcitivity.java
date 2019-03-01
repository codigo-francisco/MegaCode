package com.megacode.views.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.megacode.Claves;
import com.megacode.R;
import com.megacode.components.CustomWebChromeClient;
import com.megacode.components.WebViewJavaScriptInterface;
import com.megacode.helpers.HtmlHelper;
import com.megacode.helpers.StringHelper;
import com.megacode.ia.EmotionClassification;
import com.megacode.models.InfoNivel;
import com.megacode.models.database.Nivel;
import com.megacode.models.database.NivelTerminado;
import com.megacode.models.database.Sesion;
import com.megacode.models.database.Usuario;
import com.megacode.others.CustomCallback;
import com.megacode.ia.FaceRecognition;
import com.megacode.viewmodels.MegaCodeViewModel;
import com.megacode.views.fragments.GameFragment;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MegaCodeAcitivity extends AppCompatActivity implements  AndroidFragmentApplication.Callbacks {

	private Sesion sesionActual;
	private final static String TAG = MegaCodeAcitivity.class.getName();
	private CameraBridgeViewBase cameraBridgeViewBase;
	private FaceRecognition faceRecognition;
	private TextView textViewEmotion;
	private ImageView imageViewFace;
	private LinearLayout linearLayoutCamera;
	private WebView webView;
	private GameFragment libgdxFragment;
	private Nivel nivelActual;
	private MegaCodeViewModel megaCodeViewModel;
    private final static int idMenuCamera = 0;
    private final static int idRecargarBlockly = 1;
    private final static int idMostrarCodigo = 2;
    private WebViewJavaScriptInterface javaScriptInterface;
    private String paginaHtml;
    private Usuario usuario;

    private CameraBridgeViewBase.CvCameraViewListener2 listenerCamera = new CameraBridgeViewBase.CvCameraViewListener2() {
		@Override
		public void onCameraViewStarted(int width, int height) {

		}

		@Override
		public void onCameraViewStopped() {

		}

		@Override
		public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
			Mat frame = inputFrame.rgba();

			return faceRecognition.markAndDetectEmotion(frame, true);
		}
	};

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode==0){
            if (grantResults.length>0){
                inicializarCamara();
            }
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, idMenuCamera, Menu.NONE, "Abrir/Cerrar Camara");
        menu.add(Menu.NONE, idRecargarBlockly, Menu.NONE, "Recargar Blockly");
        menu.add(Menu.NONE, idMostrarCodigo, Menu.NONE, "Mostrar Codigo Generado");

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
        	//webView.reload();
			webView.loadUrl("about:blank");
            webView.loadDataWithBaseURL("file:///android_asset/blockly/",paginaHtml, HtmlHelper.MIME, HtmlHelper.ENCODING, null);
		}else if (idItem == idMostrarCodigo){
			javaScriptInterface.generarCodigoBlockly(new CustomCallback<String>() {
				@Override
				public void processResponse(String codigo) {
					View view = LayoutInflater.from(MegaCodeAcitivity.this).inflate(R.layout.dialog_code, findViewById(android.R.id.content), false);
					TextView text_code = view.findViewById(R.id.dialog_code_text_code);
					text_code.setText(codigo);
					text_code.setMovementMethod(new ScrollingMovementMethod());
					AlertDialog dialog = new AlertDialog.Builder(MegaCodeAcitivity.this)
							.setView(view)
							.setTitle("Codigo generado")
							.setCancelable(true)
							.show();
				}
			});
			webView.loadUrl("javascript:getCodeBlockly()"); //Se ejecuta funcion para obtener codigo generado
		}

        return super.onOptionsItemSelected(item);
    }

	private void inicializarCamara(){
		OpenCVLoader.initDebug();

		cameraBridgeViewBase.enableView();
		try {
			faceRecognition = new FaceRecognition(this, FaceRecognition.LBP_CASCADE, EmotionClassification.RAFD_MODEL);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	private Timer contador = new Timer("contadorTiempo");

	private void inicializarTiempo(){
		sesionActual.tiempo = 0;
		contador.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				sesionActual.tiempo++;
			}
		}, Claves.RETRASO_CONTADOR_TIEMPO_SESION, Claves.INTERMITENCIA_CONTADOR_TIEMPO_SESION);
	}

	@SuppressLint({"SetJavaScriptEnabled"})
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = getIntent();
		if (intent!=null){
			nivelActual = intent.getParcelableExtra("nivel");
		}

		if (nivelActual == null){
			Toast.makeText(this, "No se ha cargado la ruta del nivel correctamente",Toast.LENGTH_LONG).show();
			finish();
		}

		megaCodeViewModel = ViewModelProviders.of(this).get(MegaCodeViewModel.class);

		megaCodeViewModel.getUsuario().observe(this, usuario -> {
			this.usuario = usuario;
		});

		cameraBridgeViewBase = findViewById(R.id.camera_view);
		cameraBridgeViewBase.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
		cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
		cameraBridgeViewBase.setCvCameraViewListener(listenerCamera);

		//textViewEmotion = findViewById(R.id.text_view_emotion);
		imageViewFace = findViewById(R.id.image_view_face);
		linearLayoutCamera = findViewById(R.id.linear_layout_camera);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA }, 0);
		}else{
			inicializarCamara();
		}

		/*Button butonCamera = findViewById(R.id.button_camera);
		butonCamera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bitmap imageFace = Bitmap.createBitmap(lastFrame.width(), lastFrame.height(), Bitmap.Config.ARGB_8888);
				Utils.matToBitmap(lastFrame, imageFace);
				imageViewFace.setImageBitmap(imageFace);
				textViewEmotion.setText("Detectando emocion...");
				faceRecognition.detectEmotion(lastFrame, response -> textViewEmotion.setText(response));
			}
		});*/

		webView = findViewById(R.id.megacode_activity_webview);
		webView.setWebChromeClient(new CustomWebChromeClient());
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		paginaHtml = HtmlHelper.generarHtml(nivelActual.getRuta(), this);

		webView.loadDataWithBaseURL("file:///android_asset/blockly/",paginaHtml, HtmlHelper.MIME, HtmlHelper.ENCODING, null);

        findViewById(R.id.megacode_play).setOnClickListener(view -> {
        	sesionActual.intentos++;
        	webView.loadUrl("javascript:runBlockly()");
        });

        InfoNivel infoNivel = new InfoNivel();
        infoNivel.rutaNivel = nivelActual.getRuta();
        infoNivel.zoomInicial = nivelActual.getZoomInicial();

        cargarJuego(infoNivel);

		javaScriptInterface = new WebViewJavaScriptInterface(libgdxFragment);
		webView.addJavascriptInterface(javaScriptInterface, "megacode");
	}

	private void cargarJuego(InfoNivel infoNivel){
		// Create libgdx fragment
		libgdxFragment = new GameFragment(infoNivel);

		libgdxFragment.getGame().addLoadGameListener(() -> {
			//Se crea una sesión nueva
			sesionActual = new Sesion();
			//Se inicializa el contador de tiempo
			inicializarTiempo();

			libgdxFragment.getGamePlayScreen().addNivelCompletadoListener(screen -> {
				//Se registran todos los avances del nivel
				String cadenaOptima = nivelActual.getCadenaOptima();
				String cadenaGenerada = javaScriptInterface.getUltimoCodigoGenerado();
				int distance = StringHelper.levenshteinDistance(cadenaOptima, cadenaGenerada);
				int puntaje = (int) Math.round((double) distance / Math.max(cadenaOptima.length(), cadenaGenerada.length()) * 100);
				NivelTerminado nivelTerminado = new NivelTerminado();
				nivelTerminado.setNivelId(nivelActual.getId());
				nivelTerminado.setPuntaje(puntaje);
				nivelTerminado.setTerminado(true);
				long idUsuario = usuario.getId(); //PreferenceManager.getDefaultSharedPreferences(MegaCodeAcitivity.this).getLong(Claves.ID_USUARIO, 0);
				nivelTerminado.setUsuarioId(idUsuario);
				nivelTerminado.setNewId();
				megaCodeViewModel.insertarNivelTerminadoSync(nivelTerminado);

				//Actualizar puntajes del usuario
				if (nivelTerminado.isTerminado() && usuario != null) {
					int puntajeComandos = usuario.getComandos() + nivelActual.getComandos();
					int puntajeSi = usuario.getSi() + nivelActual.getSi();
					int puntajePara = usuario.getPara() + nivelActual.getPara();
					int puntajeMientras = usuario.getMientras() + nivelActual.getMientras();

					usuario.setComandos(puntajeComandos);
					usuario.setSi(puntajeSi);
					usuario.setPara(puntajePara);
					usuario.setMientras(puntajeMientras);

					megaCodeViewModel.actualizarUsuario(usuario);
				}

				MegaCodeAcitivity.this.setResult(Activity.RESULT_OK);
				MegaCodeAcitivity.this.finish();
			/*runOnUiThread(() -> {
				DataModel siguienteEjercicio = megaCodeViewModel.siguienteEjercicioSync();
				if (siguienteEjercicio != null) {
					libgdxFragment.handler.post(new Runnable() {
						@Override
						public void run() {
							AlertDialog alertDialog = new AlertDialog.Builder(MegaCodeAcitivity.this)
									.setTitle("Continuar los ejercicios")
									.setMessage("¿Quieres pasar a otro nivel?")
									.setPositiveButton("Si", (dialogInterface, i) -> {
										screen.dispose();
										libgdxFragment.getGame().dispose();
										libgdxFragment = null;
										//Mejor solución pendiente
										InfoNivel infoNivelSiguiente = new InfoNivel();
										infoNivelSiguiente.rutaNivel =
										cargarJuego();
									})
									.setNegativeButton("No", (dialogInterface, i) -> MegaCodeAcitivity.this.finish())
									.setCancelable(false)
									.show();
						}
					});
				} else {
					AlertDialog alertDialog = new AlertDialog.Builder(MegaCodeAcitivity.this)
							.setTitle("Ejercicio terminado")
							.setMessage("Felicidades, selecciona otro ejercicio en el menu")
							.setPositiveButton("Volver al menu", null)
							.setOnDismissListener(dialogInterface -> MegaCodeAcitivity.this.finish())
							.show();
				}
			});*/
			});
		});

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.game_fragment);

		if (fragment!=null)
			transaction.replace(R.id.game_fragment, libgdxFragment).commit();
		else
			transaction.add(R.id.game_fragment, libgdxFragment).commit();
	}

	@Override
	public void exit() {
		Log.d(TAG, "exit");
	}
}
