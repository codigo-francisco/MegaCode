package com.rockbass2560.megacode.views.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rockbass2560.megacode.Claves;
import com.rockbass2560.megacode.Funciones;
import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.base.ActivityToolbarBase;
import com.rockbass2560.megacode.comparators.ComparatorEmocion;
import com.rockbass2560.megacode.components.CustomWebChromeClient;
import com.rockbass2560.megacode.components.MediaPlayerManager;
import com.rockbass2560.megacode.components.WebViewJavaScriptInterface;
import com.rockbass2560.megacode.helpers.HtmlHelper;
import com.rockbass2560.megacode.helpers.StringHelper;
import com.rockbass2560.megacode.models.InfoNivel;
import com.rockbass2560.megacode.models.database.Nivel;
import com.rockbass2560.megacode.models.database.NivelTerminado;
import com.rockbass2560.megacode.models.database.Sesion;
import com.rockbass2560.megacode.others.CustomCallback;
import com.rockbass2560.megacode.viewmodels.MegaCodeViewModel;
import com.rockbass2560.megacode.views.fragments.GameFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MegaCodeAcitivity extends ActivityToolbarBase implements  AndroidFragmentApplication.Callbacks {

	private Sesion sesionActual;
	private final static String TAG = MegaCodeAcitivity.class.getName();
	private TextView textViewEmotion;
	private WebView webView;
	private GameFragment libgdxFragment;
	private Nivel nivelActual;
	private MegaCodeViewModel megaCodeViewModel;
    private final static int idRecargarBlockly = 1;
    private final static int idMostrarCodigo = 2;
    private WebViewJavaScriptInterface javaScriptInterface;
    private String paginaHtml;
    private SharedPreferences sharedPreferences;
    private int format;
    private Handler.Callback handler;
    private int etapa;
    private boolean reboot=false;
    private FloatingActionButton megacodePlay;
    private boolean canBack = true;
    private LinearLayout linearLayout;
    private short contadorPeticiones;

    public MegaCodeAcitivity(){
    	super(R.layout.activity_main);
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

		Intent intent = getIntent();
		if (intent != null){
			nivelActual = intent.getParcelableExtra("nivel");
		}

		if (nivelActual == null){
			Toast.makeText(this, "No se ha cargado la ruta del nivel correctamente",Toast.LENGTH_LONG).show();
			finish();
		}

		sharedPreferences = getSharedPreferences(Claves.SHARED_MEGACODE_PREFERENCES, 0);

		mediaPlayerManager.setBackActivity(true);

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

				Intent returnData = new Intent();

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
	public void exit() {
		Log.d(TAG, "exit");
	}
}
