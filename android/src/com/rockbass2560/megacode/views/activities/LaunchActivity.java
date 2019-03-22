package com.megacode.views.activities;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.megacode.R;
import com.megacode.models.database.Usuario;
import com.megacode.viewmodels.LoginViewModel;
import com.megacode.viewmodels.UsuarioViewModel;

public class LaunchActivity extends ActivityBase {

    private UsuarioViewModel usuarioViewModel;
    private LoginViewModel loginViewModel;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        usuarioViewModel = ViewModelProviders.of(this).get(UsuarioViewModel.class);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        AlertDialog alertDialog = new AlertDialog.Builder(LaunchActivity.this)
                .setMessage("Ha ocurrido un error en el proceso de login")
                .setPositiveButton("Intentar de nuevo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loginApp();
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

        loginViewModel.getUsuarioMutableLiveData().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if (usuario!=null){
                    Intent intentActivity = new Intent(LaunchActivity.this, RootActivity.class);
                    startActivity(intentActivity);
                }else{
                    alertDialog.show();
                }
            }
        });

        usuarioViewModel.obtenerUsuario().observe(this, usuario -> {
            if (usuario != null) {
                this.usuario = usuario;
                loginApp();
            }else{
                Intent intentActivity = new Intent(LaunchActivity.this, LoginActivity.class);
                startActivity(intentActivity);
            }
        });
    }

    private void loginApp(){
        //Se necesita hacer logín para poder recuperar los datos más recientes con el token
        loginViewModel.loginUsuario(usuario.email, usuario.contrasena);
    }
}
