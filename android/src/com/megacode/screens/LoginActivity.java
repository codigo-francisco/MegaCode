package com.megacode.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.button.MaterialButton;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.megacode.base.ActivityBase;
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


public class LoginActivity extends ActivityBase {

    private final static String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView loginTextRegistrate = findViewById(R.id.login_text_registrate);

        loginTextRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Abrir actividad de registro
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        MaterialButton loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText emailEditText = findViewById(R.id.activity_login_text_email);
                TextInputEditText contrasenaEditText = findViewById(R.id.activity_login_text_contrasena);

                final Persona personaLogin = new Persona();
                personaLogin.setEmail(emailEditText.getText().toString());
                personaLogin.setContrasena(contrasenaEditText.getText().toString());

                //Realizar el registro
                MegaCodeServiceInstance.getMegaCodeServiceInstance().megaCodeService.login(personaLogin).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            //Obtenemos los datos del usuario desde el servidor y establecemos el token generado
                            final Persona datosUsuario = response.body().getUsuario();
                            datosUsuario.setToken(response.body().getToken());

                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    //Buscar si hay algún usuario en Realm, eliminarlo
                                    RealmQuery<Persona> query = realm.where(Persona.class);
                                    for (Persona persona : query.findAll()) {
                                        persona.deleteFromRealm();
                                    }
                                    // registrar al usuario en bd local con su nuevo token
                                    realm.copyToRealm(datosUsuario);

                                    //Se manda a llamar la actividad principal
                                    Intent intentActivity = new Intent(getApplication(), RootActivity.class);
                                    //intentActivity.putExtra("persona", datosUsuario);
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    preferences.edit().putString(getString(R.string.persona), datosUsuario.toJson()).apply();
                                    startActivity(intentActivity);
                                }
                            });
                        } else if (response.code() == 403) {
                            Toast.makeText(getApplicationContext(), "Email o contraseña incorrectos", Toast.LENGTH_LONG).show();
                        }else{
                            errorGeneralMessage.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        errorGeneralMessage.show();
                        Log.e(TAG, t.getMessage(), t);
                    }
                });
            }
        });
    }
}
