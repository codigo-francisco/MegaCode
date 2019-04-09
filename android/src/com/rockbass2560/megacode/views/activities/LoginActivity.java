package com.rockbass2560.megacode.views.activities;

import android.app.AlertDialog;
import android.content.Intent;

import com.google.android.material.button.MaterialButton;

import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.components.MediaPlayerManager;
import com.rockbass2560.megacode.helpers.ViewHelper;
import com.rockbass2560.megacode.viewmodels.LoginViewModel;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;


public class LoginActivity extends FragmentActivity {

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

        loginButton = findViewById(R.id.button_login);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginTextRegistrate.setOnClickListener(view -> {
            //Abrir actividad de registro
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        loginViewModel.observadorUsuario().observe(this, user -> {
            progressBar.setVisibility(ProgressBar.GONE);
            loginButton.setEnabled(true);

            if (user!=null){
                Intent intent = new Intent(this, RootActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        AlertDialog.Builder builderDialog = new AlertDialog.Builder(this)
                .setTitle("No se ha realizado el login")
                .setPositiveButton("Ok", null);

        loginViewModel.observadorError().observe(this, error ->{
            loginButton.setEnabled(true);
            progressBar.setVisibility(ProgressBar.GONE);

            builderDialog.setMessage(error)
                    .show();
        });

        loginButton.setOnClickListener(view -> {
            TextInputEditText emailEditText = findViewById(R.id.activity_login_text_email);
            TextInputEditText contrasenaEditText = findViewById(R.id.activity_login_text_contrasena);

            String email = emailEditText.getText().toString();
            String contrasena = contrasenaEditText.getText().toString();

            ViewHelper.closeKeyboard(this, view.getWindowToken());

            if (!email.isEmpty() && !contrasena.isEmpty()) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                loginButton.setEnabled(false);
                loginViewModel.loginUsuario(email, contrasena);
            }else{
                Toast.makeText(this, "Necesita ingresa un usuario y contraseña", Toast.LENGTH_LONG).show();
            }
        });
    }
}
