package com.megacode.views.activities;

import android.content.Intent;

import com.google.android.material.button.MaterialButton;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.megacode.R;
import com.megacode.base.LoginApp;
import com.megacode.models.IDialog;
import com.megacode.models.database.Usuario;


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

        datosIncorrectosDialog = new IDialog() {
            @Override
            public void show() {
                progressBar.setVisibility(ProgressBar.GONE);
                loginButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Email o contraseña incorrectos", Toast.LENGTH_LONG).show();
            }
        };

        loginButton.setOnClickListener(view -> {
            TextInputEditText emailEditText = findViewById(R.id.activity_login_text_email);
            TextInputEditText contrasenaEditText = findViewById(R.id.activity_login_text_contrasena);

            //Se manda a llamar la actividad principal, se crea un task nuevo para borrar la actividad actual
            Intent intentActivity = new Intent(LoginActivity.this, RootActivity.class);
            intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            progressBar.setVisibility(ProgressBar.VISIBLE);
            loginButton.setEnabled(false);

            String email = emailEditText.getText().toString();
            String contrasena = contrasenaEditText.getText().toString();

            loginApp(email, contrasena, intentActivity);
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
