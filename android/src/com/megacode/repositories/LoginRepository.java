package com.megacode.repositories;

import android.app.Application;
import android.util.Log;

import com.megacode.dao.UsuarioDao;
import com.megacode.databases.DataBaseMegaCode;
import com.megacode.models.RegistroResponse;
import com.megacode.models.database.Usuario;
import com.megacode.models.response.LoginResponse;
import com.megacode.services.MegaCodeService;
import com.megacode.services.interfaces.LoginService;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private final static String TAG = LoginRepository.class.getName();
    private UsuarioRepository usuarioRepository;
    private MutableLiveData<Usuario> usuarioLiveData = new MutableLiveData<>();

    public LoginRepository(Application application){
        usuarioRepository = new UsuarioRepository(application);
    }

    public MutableLiveData<Usuario> loginUsuario(String email, String contrasena){

        //Login remoto
        MegaCodeService.getServicio(LoginService.class).login(new Usuario(email, contrasena)).
                enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    Log.v(TAG, "Obtencion de datos del usuario correcto");

                    String token = response.body().getToken();
                    Usuario usuario = response.body().getUsuario();
                    usuario.setToken(token);

                    usuarioRepository.insert(usuario);
                    usuarioLiveData.postValue(usuario);
                }else{
                    Usuario usuario = new Usuario();
                    usuario.setErrorCode(response.code());
                    //usuario.setError(response.errorBody().string());

                    usuarioLiveData.postValue(usuario);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
                if (hasUserInDB()){
                    usuarioLiveData.postValue(usuarioRepository.obtenerUsuario().getValue());
                }else {
                    usuarioLiveData.postValue(null);
                }
            }
        });

        return usuarioLiveData;
    }

    public MutableLiveData<Usuario> registrar(Usuario usuario){

        MegaCodeService.getServicio(LoginService.class).registrar(usuario).enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {

                if (response.isSuccessful()){
                    Log.v(TAG, "Datos registrados en base de datos a través de servidor");

                    RegistroResponse registroResponse = response.body();

                    usuario.setId(registroResponse.getId());
                    usuario.setToken(registroResponse.getToken());

                    usuarioRepository.insert(usuario);
                }else{
                    int errorCode = response.code();
                    usuario.setErrorCode(errorCode);
                }

                usuarioLiveData.postValue(usuario);
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
                usuarioLiveData.postValue(null);
            }
        });

        return usuarioLiveData;
    }

    public MutableLiveData<Usuario> getUsuarioLiveData(){
        return usuarioLiveData;
    }

    private boolean hasUserInDB(){
        return usuarioRepository.cantidadUsuario() > 0;
    }
}
