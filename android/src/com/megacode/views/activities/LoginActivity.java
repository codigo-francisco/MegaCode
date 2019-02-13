package com.megacode.views.activities;

import android.content.Intent;

import com.google.android.material.button.MaterialButton;

import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.megacode.R;
import com.megacode.models.database.Usuario;
import com.megacode.viewmodels.LoginViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class LoginActivity extends AppCompatActivity {

    private final static String TAG = LoginActivity.class.getName();

    private ProgressBar progressBar;
    private MaterialButton loginButton;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView loginTextRegistrate = findViewById(R.id.login_text_registrate);
        progressBar = findViewById(R.id.login_progressbar);

        loginTextRegistrate.setOnClickListener(view -> {
            //Abrir actividad de registro
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });

        loginButton = findViewById(R.id.button_login);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginViewModel.getUsuarioMutableLiveData().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if (usuario!=null){
                    if (!usuario.hasError()) {
                        abrirRootActivity();
                    }else{
                        //Se busca un usuario en Base de datos y se carga offline
                        AsyncTask.execute(()->{
                            Usuario usuarioBD = loginViewModel.getUsuario();
                            if (usuarioBD != null){
                                abrirRootActivity();
                            }else {
                                if (usuario.getErrorCode() == 403) {
                                    mostrarMensajeError("Email o contraseña incorrectos");
                                } else {
                                    mostrarMensajeError("Ha ocurrido un error en el proceso");
                                }
                            }
                        });
                    }
                } else {
                    mostrarMensajeError("Ha ocurrido un error en el proceso");
                }
            }
        });

        loginButton.setOnClickListener(view -> {
            TextInputEditText emailEditText = findViewById(R.id.activity_login_text_email);
            TextInputEditText contrasenaEditText = findViewById(R.id.activity_login_text_contrasena);

            progressBar.setVisibility(ProgressBar.VISIBLE);
            loginButton.setEnabled(false);

            String email = emailEditText.getText().toString();
            String contrasena = contrasenaEditText.getText().toString();

            loginViewModel.loginUsuario(email, contrasena);
        });
    }

    private void abrirRootActivity(){
        //Se registra la nueva conexion
        AsyncTask.execute(()-> loginViewModel.registrarNuevaConexion());

        //Se manda a llamar la actividad principal, se crea un task nuevo para borrar la actividad actual
        Intent intentActivity = new Intent(LoginActivity.this, RootActivity.class);
        intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentActivity);
    }

    private void mostrarMensajeError(String mensaje){
        progressBar.setVisibility(ProgressBar.GONE);
        loginButton.setEnabled(true);
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
