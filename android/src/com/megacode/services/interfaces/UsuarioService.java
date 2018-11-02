package com.megacode.services.interfaces;

import com.megacode.models.database.Usuario;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UsuarioService {
    @POST("api/usuario/registrarFoto")
    Call<ResponseBody> registrarFotoUsuario(@Header("Authorization") String token, @Body Usuario usuario);
}
