package com.megacode.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.button.MaterialButton;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.megacode.base.ActivityBase;
import com.megacode.base.LoginApp;
import com.megacode.models.IDialog;
import com.megacode.models.Persona;
import com.megacode.models.response.LoginResponse;
import com.megacode.services.MegaCodeServiceInstance;

import java.util.Set;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends LoginApp {

    private final static String TAG = "LoginActivity";

    private ProgressBar progressBar;
    private MaterialButton loginButton;

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

        loginButton.setOnClickListener(view -> {
            TextInputEditText emailEditText = findViewById(R.id.activity_login_text_email);
            TextInputEditText contrasenaEditText = findViewById(R.id.activity_login_text_contrasena);

            Persona personaLogin = new Persona();
            personaLogin.setEmail(emailEditText.getText().toString());
            personaLogin.setContrasena(contrasenaEditText.getText().toString());

            //Se manda a llamar la actividad principal, se crea un task nuevo para borrar la actividad actual
            Intent intentActivity = new Intent(LoginActivity.this, RootActivity.class);
            intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            progressBar.setVisibility(ProgressBar.VISIBLE);
            loginButton.setEnabled(false);

            loginApp(personaLogin, intentActivity);
        });
    }

    @Override
    public IDialog createDialog() {
        return () -> {
            progressBar.setVisibility(ProgressBar.GONE);
            loginButton.setEnabled(true);
            errorGeneralMessage.show();
        };
    }
}
