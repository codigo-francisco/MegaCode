package com.megacode.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.megacode.R;
import com.megacode.base.LoginApp;
import com.megacode.models.IDialog;
import com.megacode.models.database.Usuario;
import com.megacode.viewmodels.UsuarioViewModel;

import java.util.concurrent.Executors;

public class LaunchActivity extends LoginApp {

    private UsuarioViewModel usuarioViewModel;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        usuarioViewModel = ViewModelProviders.of(this).get(UsuarioViewModel.class);

        usuarioViewModel.obtenerUsuario().observe(this, usuario -> {
            if (usuario != null) {
                Intent intentActivity = new Intent(LaunchActivity.this, RootActivity.class);
                loginApp(usuario.email, usuario.contrasena, intentActivity);
            }else{
                Intent intentActivity = new Intent(LaunchActivity.this, LoginActivity.class);
                startActivity(intentActivity);
            }
        });
    }

    @Override
    public IDialog createDialog() {
        return new IDialog() {
            @Override
            public void show() {
                AlertDialog alertDialog = new AlertDialog.Builder(LaunchActivity.this)
                        .setMessage("Ha ocurrido un error en el proceso de login")
                        .setPositiveButton("Intentar de nuevo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                usuarioViewModel.obtenerUsuario();
                            }
                        })
                        .setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LaunchActivity.this.finish();
                            }
                        })
                        .setCancelable(false)
                        .create();
                alertDialog.show();
            }
        };
    }
}
