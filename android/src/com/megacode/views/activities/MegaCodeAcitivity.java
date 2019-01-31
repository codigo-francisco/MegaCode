package com.megacode.views.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import androidx.lifecycle.ViewModelProviders;

import android.preference.PreferenceManager;
import android.text.TextUtils;
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

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.megacode.Claves;
import com.megacode.Comando;
import com.megacode.MegaCodeGame;
import com.megacode.R;
import com.megacode.components.CustomWebChromeClient;
import com.megacode.helpers.HtmlHelper;
import com.megacode.helpers.StringHelper;
import com.megacode.models.InfoNivel;
import com.megacode.models.database.Nivel;
import com.megacode.models.database.NivelTerminado;
import com.megacode.others.FaceRecognition;
import com.megacode.viewmodels.MegaCodeViewModel;
import com.megacode.views.fragments.GameFragment;
import com.megacode.GameplayScreen;
import com.megacode.Level;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MegaCodeAcitivity extends AppCompatActivity implements  AndroidFragmentApplication.Callbacks, CameraBridgeViewBase.CvCameraViewListener2 {

	private final static String TAG = "MegaCodeActivity";
	private CameraBridgeViewBase cameraBridgeViewBase;
	private FaceRecognition faceRecognition;
	private TextView textViewEmotion;
	private ImageView imageViewFace;
	private LinearLayout linearLayoutCamera;
	private WebView webView;
	private GameFragment libgdxFragment;
	private WebViewJavaScriptInterface javaScriptInterface;
	private Nivel nivelActual;
	private MegaCodeViewModel megaCodeViewModel;
    private List<Character> ultimoCodigoGenerado = new ArrayList<>();
    private final static int idMenuCamera = 0;
    private final static int idRecargarBlockly = 1;
    private String paginaHtml;

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
		}

        return super.onOptionsItemSelected(item);
    }

	private void inicializarCamara(){
		OpenCVLoader.initDebug();

		cameraBridgeViewBase.enableView();
		faceRecognition = new FaceRecognition(getApplicationContext());
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
				faceRecognition.detectEmotion(lastFrame, response -> textViewEmotion.setText(response));
			}
		});

		webView = findViewById(R.id.megacode_activity_webview);
		webView.setWebChromeClient(new CustomWebChromeClient());
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

        javaScriptInterface = new WebViewJavaScriptInterface();
		webView.addJavascriptInterface(javaScriptInterface, "megacode");

		paginaHtml = HtmlHelper.generarHtml(nivelActual.getRuta(), this);

		webView.loadDataWithBaseURL("file:///android_asset/blockly/",paginaHtml, HtmlHelper.MIME, HtmlHelper.ENCODING, null);

        findViewById(R.id.megacode_play).setOnClickListener(view -> webView.loadUrl("javascript:runBlockly()"));

        InfoNivel infoNivel = new InfoNivel();
        infoNivel.rutaNivel = nivelActual.getRuta();
        infoNivel.zoomInicial = nivelActual.getZoomInicial();

        cargarJuego(infoNivel);
	}

	private void cargarJuego(InfoNivel infoNivel){
		// Create libgdx fragment
		libgdxFragment = new GameFragment(infoNivel);

		libgdxFragment.getGame().addLoadGameListener(new MegaCodeGame.LoadGameListener() {
            @Override
            public void loadedGame() {
                libgdxFragment.getGamePlayScreen().addNivelCompletadoListener(new GameplayScreen.NivelCompletadoListener() {
                    @Override
                    public void nivelTerminado(GameplayScreen screen) {
                        //Se registran todos los avances del nivel
                        String cadenaOptima = nivelActual.getCadenaOptima();
                        String cadenaGenerada = TextUtils.join("", ultimoCodigoGenerado);
                        int distance = StringHelper.levenshteinDistance(cadenaOptima, cadenaGenerada);
                        int puntaje = (int) Math.round((double) distance / Math.max(cadenaOptima.length(), cadenaGenerada.length()) * 100);
                        NivelTerminado nivelTerminado = new NivelTerminado();
                        nivelTerminado.setNivelId(nivelActual.getId());
                        nivelTerminado.setPuntaje(puntaje);
                        nivelTerminado.setTerminado(true);
                        long idUsuario = PreferenceManager.getDefaultSharedPreferences(MegaCodeAcitivity.this).getLong(Claves.ID_USUARIO, 0);
                        nivelTerminado.setUsuarioId(idUsuario);
                        nivelTerminado.setNewId();
                        megaCodeViewModel.insertarNivelTerminadoSync(nivelTerminado);

                        //Registrar avance en puntaje del estudia TODO: pendiente

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

    private void agregarCodigo(Comando comando){
        switch (comando.getValue()){
            case "izquierda":
                ultimoCodigoGenerado.add('A');
                break;
            case "derecha":
                ultimoCodigoGenerado.add('B');
                break;
            case "saltar":
                ultimoCodigoGenerado.add('C');
                break;
            case "disparar":
                ultimoCodigoGenerado.add('D');
                break;
        }
	}

    class WebViewJavaScriptInterface{

		@JavascriptInterface
		public boolean respawn(){
			return libgdxFragment.getGamePlayScreen().level.megaCode.respawn;
		}

		@JavascriptInterface
		public boolean enemigoDeFrente(){
			Level level = libgdxFragment.getGamePlayScreen().level;

			return level.enemyInFront;
		}

		@JavascriptInterface
        public boolean caminarDerecha(){
            return ejecutarComando(Comando.CAMINAR_DERECHA);
        }

        @JavascriptInterface
        public boolean caminarIzquierda(){
            return ejecutarComando(Comando.CAMINAR_IZQUIERDA);
        }

        @JavascriptInterface
        public boolean disparar(){
            return ejecutarComando(Comando.DISPARAR);
        }

        @JavascriptInterface
        public boolean saltar(){
            return ejecutarComando(Comando.SALTAR);
        }

        @JavascriptInterface
        public boolean recibirComandos(){
		    return libgdxFragment.getGamePlayScreen().level.recibirComandos;
        }

        @JavascriptInterface
		public void prepararNivel(){
		    ultimoCodigoGenerado.clear();
			libgdxFragment.getGamePlayScreen().level.prepararNivel();
		}

        private boolean ejecutarComando(Comando comando){
            agregarCodigo(comando);

            Level level = libgdxFragment.getGamePlayScreen().level;

            Log.d(TAG, "Comando llamado: "+comando.name());

            level.procesarComando(comando);

            return level.recibirComandos;
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
