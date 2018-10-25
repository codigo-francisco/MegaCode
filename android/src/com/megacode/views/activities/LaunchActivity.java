package com.megacode.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.megacode.R;
import com.megacode.base.LoginApp;
import com.megacode.models.IDialog;
import com.megacode.models.database.Usuario;
import com.megacode.viewmodels.UsuarioViewModel;

public abstract class LaunchActivity extends LoginApp {

    private class LoginTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Usuario usuario = usuarioViewModel.obtenerUsuario().getValue();

            if (usuario!=null) {
                Intent intentActivity = new Intent(LaunchActivity.this, RootActivity.class);
                loginApp(usuario, intentActivity);
            }else{
                Intent intentActivity = new Intent(LaunchActivity.this, LoginActivity.class);
                startActivity(intentActivity);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private LoginTask loginTask;
    private UsuarioViewModel usuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        usuarioViewModel = ViewModelProviders.of(this).get(UsuarioViewModel.class);

        loginTask = new LoginTask();
        loginTask.execute();
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
                                loginTask = new LoginTask();
                                loginTask.execute();
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

    @Override
    protected void onDestroy() {
        if (!loginTask.isCancelled())
            loginTask.cancel(true);
        loginTask = null;
        super.onDestroy();
    }
}
