package com.megacode.views.activities;

import android.content.Intent;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.megacode.R;
import com.megacode.viewmodels.UsuarioViewModel;

public class LaunchActivity extends ActivityBase {

    private UsuarioViewModel usuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        usuarioViewModel = ViewModelProviders.of(this).get(UsuarioViewModel.class);

        usuarioViewModel.obtenerUsuario().observe(this, usuario -> {
            if (usuario != null) {
                Intent intentActivity = new Intent(LaunchActivity.this, RootActivity.class);
                startActivity(intentActivity);
            }else{
                Intent intentActivity = new Intent(LaunchActivity.this, LoginActivity.class);
                startActivity(intentActivity);
            }
        });
    }
}
