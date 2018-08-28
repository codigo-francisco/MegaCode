package com.megacode.services;

import com.megacode.models.Persona;
import com.megacode.models.RegistroResponse;
import com.megacode.models.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MegaCodeService {

    @POST("api/autenticacion/registrar")
    Call<RegistroResponse> registrar(@Body Persona usuario);

    @POST("api/autenticacion/login")
    Call<LoginResponse> login(@Body Persona usuario);

}
