package com.megacode.screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.megacode.base.LoginApp;
import com.megacode.models.IDialog;
import com.megacode.models.Persona;

import io.realm.Realm;
import io.realm.RealmQuery;

public class LaunchActivity extends LoginApp {

    private class LoginTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //Se valida que exista el usuario, en caso de existir se pasa a la pantalla principal
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<Persona> realmQuery = realm.where(Persona.class);

            if (realmQuery.count()>0) {
                Persona persona = realmQuery.findFirst();
                persona = persona.buildPersonaObj();

                realm.close();

                Intent intentActivity = new Intent(LaunchActivity.this, RootActivity.class);
                loginApp(persona, intentActivity);
            }else{
                realm.close();

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

    LoginTask loginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

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
