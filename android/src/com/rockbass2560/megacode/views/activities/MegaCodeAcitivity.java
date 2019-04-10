package com.rockbass2560.megacode.views.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageWriter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.collect.Lists;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.rockbass2560.megacode.Claves;
import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.base.ActivityToolbarBase;
import com.rockbass2560.megacode.components.CustomWebChromeClient;
import com.rockbass2560.megacode.components.MediaPlayerManager;
import com.rockbass2560.megacode.components.WebViewJavaScriptInterface;
import com.rockbass2560.megacode.helpers.HtmlHelper;
import com.rockbass2560.megacode.helpers.StringHelper;
import com.rockbass2560.megacode.ia.CameraManagerIA;
import com.rockbass2560.megacode.ia.EmotionClassification;
import com.rockbass2560.megacode.models.InfoNivel;
import com.rockbass2560.megacode.models.database.Emocion;
import com.rockbass2560.megacode.models.database.Nivel;
import com.rockbass2560.megacode.models.database.NivelTerminado;
import com.rockbass2560.megacode.models.database.Sesion;
import com.rockbass2560.megacode.models.Usuario;
import com.rockbass2560.megacode.others.CustomCallback;
import com.rockbass2560.megacode.ia.FaceRecognition;
import com.rockbass2560.megacode.viewmodels.MegaCodeViewModel;
import com.rockbass2560.megacode.views.fragments.GameFragment;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MegaCodeAcitivity extends ActivityToolbarBase implements  AndroidFragmentApplication.Callbacks {

	private Sesion sesionActual;
	private final static String TAG = MegaCodeAcitivity.class.getName();
	private final static int REQUEST_CAMERA = 0;
	private TextView textViewEmotion;
	private WebView webView;
	private GameFragment libgdxFragment;
	private Nivel nivelActual;
	private MegaCodeViewModel megaCodeViewModel;
    private final static int idMenuCamera = 0;
    private final static int idRecargarBlockly = 1;
    private final static int idMostrarCodigo = 2;
    private WebViewJavaScriptInterface javaScriptInterface;
    private String paginaHtml;
    private SharedPreferences sharedPreferences;
    private int format;
    private CameraManagerIA cameraManagerIA;
    private Handler.Callback handler;
    private int etapa;
    private boolean reboot=false;
    private FloatingActionButton megacodePlay;
    private boolean canBack = true;
    private LinearLayout linearLayout;
    private AlertDialog alertDialogErrorCamera;
    private short contadorPeticiones;

    public MegaCodeAcitivity(){
    	super(R.layout.activity_main);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode==REQUEST_CAMERA){
            if (Arrays.stream(grantResults).anyMatch(grant -> grant == PackageManager.PERMISSION_GRANTED)){
				crearCameraManager();
            }else{
            	contadorPeticiones++;
            	if (contadorPeticiones > 5){
            		alertDialogErrorCamera.show();
				}else {
					inicializarCamara();
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
    	if (canBack) {
			super.onBackPressed();
		}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
        //menu.add(Menu.NONE, idRecargarBlockly, Menu.NONE, "Recargar Blockly");
        menu.add(Menu.NONE, idMostrarCodigo, Menu.NONE, "Mostrar Codigo Generado");

        MenuItem reloadItem = menu.findItem(R.id.button_reload);
        reloadItem.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idItem = item.getItemId();

        if (idItem==R.id.button_reload){
        	//webView.reload();
			webView.loadUrl("about:blank");
            webView.loadDataWithBaseURL("file:///android_asset/blockly/",paginaHtml, HtmlHelper.MIME, HtmlHelper.ENCODING, null);
			libgdxFragment.getGamePlayScreen().level.reposicionarPersonaje();
			canBack = true;
			megacodePlay.setEnabled(true);
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

		mediaPlayerSoundClick.seekTo(0);
		mediaPlayerSoundClick.start();

        return super.onOptionsItemSelected(item);
    }

	private void inicializarCamara(){
    	if (checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED) {
    		if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
    			final String explicacion = "MegaCode toma fotografía de tu rostro mientras trabajas con el sistema por lo que es necesario utilizar la camara del dispositivo.\n\n" +
						"Estas fotografías se utilizan nada más con fines cientificos y no se publicaran en ningún sitio.";
    			new AlertDialog.Builder(this)
						.setCancelable(false)
						.setMessage(explicacion)
						.setPositiveButton("Solicitar permisos de nuevo", ((dialog, which) -> {
							requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
						}))
				.show();
			}else{
    			requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
			}
		}else{
    		crearCameraManager();
		}
	}

	private void crearCameraManager(){
		if (cameraManagerIA == null) {
			handler = new Handler.Callback(){
				@Override
				public boolean handleMessage(Message msg) {
					boolean result = false;

					if (msg.what == Claves.EMOTION_FOUND){
						Bundle bundle = msg.getData();
						String emotion = bundle.getString(Claves.EMOTION);
						Emocion emocion = new Emocion();
						emocion.etapa = etapa;
						emocion.label = emotion;
						emocion.momento = Timestamp.now();
						sesionActual.emociones.add(emocion);

						result = true;
					}

					return result;
				}
			};
			try {
				cameraManagerIA = new CameraManagerIA(this, handler);
			}catch(Exception ex){
				alertDialogErrorCamera.show();
			}
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
		}, Claves.RETRASO_CONTADOR_TIEMPO_SESION,
				Claves.INTERMITENCIA_CONTADOR_TIEMPO_SESION);
	}

	private Handler _idleHandler = new Handler();
	Runnable _idleRunnable = new Runnable() {
		@Override
		public void run() {
			sesionActual.inactividad+=10;
			delayedIdle();
		}
	};

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		delayedIdle();
	}

	private void delayedIdle(){
		_idleHandler.removeCallbacks(_idleRunnable);
		_idleHandler.postDelayed(_idleRunnable, Claves.REPETICION_INTERACCION_USUARIO);
	}

	@SuppressLint({"SetJavaScriptEnabled"})
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		etapa = 1;

		alertDialogErrorCamera = new AlertDialog.Builder(this)
				.setCancelable(false)
				.setMessage("El permiso de la camara está deshabilitado, habilita este permiso para poder utilizar megacode")
				.setPositiveButton("Está bien", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).create();

		sharedPreferences = getSharedPreferences(Claves.SHARED_MEGACODE_PREFERENCES, 0);

		mediaPlayerManager.setBackActivity(true);

		inicializarCamara();

		Intent intent = getIntent();
		if (intent!=null){
			nivelActual = intent.getParcelableExtra("nivel");
		}

		if (nivelActual == null){
			Toast.makeText(this, "No se ha cargado la ruta del nivel correctamente",Toast.LENGTH_LONG).show();
			finish();
		}

		megaCodeViewModel = ViewModelProviders.of(this).get(MegaCodeViewModel.class);

		//webView = findViewById(R.id.megacode_activity_webview);
        webView = findViewById(R.id.megacode_activity_webview);

		webView.setWebChromeClient(new CustomWebChromeClient());
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		paginaHtml = HtmlHelper.generarHtml(nivelActual.ruta, this);

		webView.loadDataWithBaseURL("file:///android_asset/blockly/",paginaHtml, HtmlHelper.MIME, HtmlHelper.ENCODING, null);

        megacodePlay = findViewById(R.id.megacode_play);

		megacodePlay.setOnClickListener(view -> {
			if (reboot){
				megacodePlay.setImageResource(R.drawable.ic_outline_play);
				libgdxFragment.getGamePlayScreen().level.reposicionarPersonaje();
				reboot = false;
			}else{
				if (!reboot) { //En caso de doble click
					canBack = false;
					etapa = 2;
					sesionActual.intentos++;
					megacodePlay.setEnabled(false);
					//megacodePlay.setVisibility(View.GONE);
					webView.loadUrl("javascript:runBlockly()");
				}
			}
        });

        InfoNivel infoNivel = nivelActual.buildInfoNivel();

        cargarJuego(infoNivel);
        configurarMusica(nivelActual);
	}

	private void reiniciarControles(){
		etapa = 1;
		reboot = true;
		//Cambiar icono
		runOnUiThread(()->{
			//megacodePlay.setVisibility(View.VISIBLE);
			megacodePlay.setEnabled(true);
			megacodePlay.setImageResource(R.drawable.ic_reload);
			canBack = true;
		});
	}

	private void configurarMusica(Nivel nivel){
		if (nivel.tipoNivel == Nivel.TIPO_COMANDOS){
			mediaPlayerManager.cambiarCancion(MediaPlayerManager.MUSICA_COMANDOS);
		}else if (nivel.tipoNivel == Nivel.TIPO_SI){
			mediaPlayerManager.cambiarCancion(MediaPlayerManager.MUSICA_SI);
		}else if (nivel.tipoNivel == Nivel.TIPO_PARA){
			mediaPlayerManager.cambiarCancion(MediaPlayerManager.MUSICA_PARA);
		}else if (nivel.tipoNivel == Nivel.TIPO_MIENTRAS){
			mediaPlayerManager.cambiarCancion(MediaPlayerManager.MUSICA_MIENTRAS);
		}
	}

	private void cargarJuego(InfoNivel infoNivel){
		// Create libgdx fragment
		libgdxFragment = new GameFragment(infoNivel);
		if (javaScriptInterface==null) {
			javaScriptInterface = new WebViewJavaScriptInterface();
			webView.addJavascriptInterface(javaScriptInterface, Claves.JAVASCRIPT_INTERFACE);
		}
		javaScriptInterface.changeLibgdxFragment(libgdxFragment);

		libgdxFragment.getGame().addLoadGameListener(() -> {
			//Se crea una sesión nueva
			if (sesionActual == null)
				sesionActual = new Sesion();
			else{
				sesionActual.intentos++;
			}
			//Se inicializa el contador de tiempo
			inicializarTiempo();

			libgdxFragment.getGamePlayScreen().addEjecucionNivelCompletado(()->{
				reiniciarControles();
			});

			libgdxFragment.getGamePlayScreen().addPersonajeMurio(()->{
				reiniciarControles();
			});

			libgdxFragment.getGamePlayScreen().addNivelCompletadoListener(screen -> {
			    etapa = 3;
				//Se registran todos los avances del nivel
				String cadenaOptima = nivelActual.cadenaOptima;
				String cadenaGenerada = javaScriptInterface.getUltimoCodigoGenerado();
				int distance = StringHelper.levenshteinDistance(cadenaOptima, cadenaGenerada);
				int puntaje = Math.round(cadenaOptima.length() / (float)(cadenaOptima.length() + distance)*100);

				NivelTerminado nivelTerminado = new NivelTerminado();
				nivelTerminado.nivelId = nivelActual.id;
				nivelTerminado.puntaje = puntaje;
				nivelTerminado.terminado  = true;

				//Se registran los nuevos puntajes
				Map<String, Integer> puntajes = new HashMap<>();
				puntajes.put("comandos", nivelActual.comandos);
				puntajes.put("si", nivelActual.si);
				puntajes.put("para", nivelActual.para);
				puntajes.put("mientras", nivelActual.mientras);

				megaCodeViewModel.agregarNivelTerminado(nivelTerminado, puntajes);

				//Guardar sesión nueva
				sesionActual.nivelId = nivelActual.id;
				sesionActual.conexionId = sharedPreferences.getString(Claves.CONEXION_ID, Claves.EMPTY_STRING);
				megaCodeViewModel.agregarSesion(sesionActual);

				MegaCodeAcitivity.this.setResult(Activity.RESULT_OK);
				MegaCodeAcitivity.this.finish();
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
    protected void onResume() {
		super.onResume();

		if (cameraManagerIA != null && !cameraManagerIA.isRunning)
			cameraManagerIA.iniciarCamara();
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (cameraManagerIA!=null){
			cameraManagerIA.cerrarCamara();
		}
	}

	@Override
    protected void onStop() {
        super.onStop();

        if (cameraManagerIA != null)
            cameraManagerIA.cerrarTodo();

        //Detener ejecución del webview
    }

	@Override
	protected void onDestroy() {
		/*linearLayout.removeView(webView);
		webView.removeAllViews();
		webView.destroy();*/

		super.onDestroy();
	}

	@Override
	public void exit() {
		Log.d(TAG, "exit");
	}
}
