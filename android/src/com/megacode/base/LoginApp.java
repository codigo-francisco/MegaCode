package com.megacode.base;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.megacode.models.Persona;
import com.megacode.models.response.LoginResponse;
import com.megacode.screens.LoginActivity;
import com.megacode.screens.RootActivity;
import com.megacode.services.MegaCodeServiceInstance;

import io.realm.Realm;
import io.realm.RealmQuery;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginApp extends ActivityBase{

    private final static String TAG = "ActivityBase";

    public void loginApp(Persona persona, Intent intent){
        //Realizar el registro
        MegaCodeServiceInstance.getMegaCodeServiceInstance().megaCodeService.login(persona).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    //Obtenemos los datos del usuario desde el servidor y establecemos el token generado
                    Persona datosUsuario = response.body().getUsuario();
                    datosUsuario.setToken(response.body().getToken());

                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();

                    //Buscar si hay algún usuario en Realm, eliminarlo
                    RealmQuery<Persona> query = realm.where(Persona.class);
                    for (Persona persona : query.findAll()) {
                        persona.deleteFromRealm();
                    }
                    // registrar al usuario en bd local con su nuevo token
                    realm.copyToRealm(datosUsuario);

                    realm.commitTransaction();
                    realm.close();

                    startActivity(intent);

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

}
