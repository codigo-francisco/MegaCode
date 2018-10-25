package com.megacode.base;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.megacode.models.IDialog;
import com.megacode.models.database.Usuario;
import com.megacode.models.response.LoginResponse;
import com.megacode.viewmodels.UsuarioViewModel;
import com.megacode.views.activities.ActivityBase;
import com.megacode.services.MegaCodeServiceInstance;

import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class LoginApp extends ActivityBase {

    private final static String TAG = "ActivityBase";

    protected IDialog errorDialog;
    protected IDialog datosIncorrectosDialog;

    protected UsuarioViewModel usuarioViewModel;

    public LoginApp(){
        super();
        errorDialog = createDialog();
    }

    public abstract IDialog createDialog();

    public void loginApp(Usuario usuario, Intent intent){
        usuarioViewModel = ViewModelProviders.of(this).get(UsuarioViewModel.class);

        //Realizar el registro
        MegaCodeServiceInstance.getMegaCodeServiceInstance().megaCodeService.login(usuario).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    //Obtenemos los datos del usuario desde el servidor y establecemos el token generado
                    Usuario datosUsuario = response.body().getUsuario();
                    datosUsuario.setToken(response.body().getToken());

                    usuarioViewModel.borrarUsuario();
                    usuarioViewModel.insert(datosUsuario);

                    startActivity(intent);

                } else if (response.code() == 403) {
                    if (datosIncorrectosDialog==null)
                        Toast.makeText(getApplicationContext(), "Email o contraseña incorrectos", Toast.LENGTH_LONG).show();
                    else
                        datosIncorrectosDialog.show();
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
        super.onDestroy();
    }
}
