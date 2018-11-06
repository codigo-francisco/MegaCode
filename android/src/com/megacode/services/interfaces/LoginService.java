package com.megacode.services.interfaces;

import com.megacode.models.response.RegistroResponse;
import com.megacode.models.database.Usuario;
import com.megacode.models.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("api/autenticacion/registrar")
    Call<RegistroResponse> registrar(@Body Usuario usuario);

    @POST("api/autenticacion/login")
    Call<LoginResponse> login(@Body Usuario usuario);
}
