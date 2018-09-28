package com.megacode.base;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.megacode.models.IDialog;
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

public abstract class LoginApp extends ActivityBase{

    private final static String TAG = "ActivityBase";

    Realm realm;

    IDialog errorDialog;

    public LoginApp(){
        super();
        errorDialog = createDialog();
    }

    public abstract IDialog createDialog();

    public void loginApp(Persona persona, Intent intent){
        //Realizar el registro
        MegaCodeServiceInstance.getMegaCodeServiceInstance().megaCodeService.login(persona).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    //Obtenemos los datos del usuario desde el servidor y establecemos el token generado
                    Persona datosUsuario = response.body().getUsuario();
                    datosUsuario.setToken(response.body().getToken());

                    if (realm==null)
                        realm = Realm.getDefaultInstance();

                    realm.beginTransaction();

                    //Buscar si hay algún usuario en Realm, eliminarlo
                    RealmQuery<Persona> query = realm.where(Persona.class);
                    for (Persona persona : query.findAll()) {
                        persona.deleteFromRealm();
                    }
                    // registrar al usuario en bd local con su nuevo token
                    realm.copyToRealm(datosUsuario);

                    realm.commitTransaction();

                    startActivity(intent);

                } else if (response.code() == 403) {
                    Toast.makeText(getApplicationContext(), "Email o contraseña incorrectos", Toast.LENGTH_LONG).show();
                }else{
                    errorDialog.show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                errorDialog.show();
                Log.e(TAG, t.getMessage(), t);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (realm!=null)
            realm.close();
        super.onDestroy();
    }
}
