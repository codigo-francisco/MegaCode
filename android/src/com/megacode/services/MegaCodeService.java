package com.megacode.services;

import com.megacode.models.Persona;
import com.megacode.models.RegistroResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MegaCodeService {

    @POST("api/login/registrar")
    Call<RegistroResponse> registrar(@Body Persona usuario);

}
