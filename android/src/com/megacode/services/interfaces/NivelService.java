package com.megacode.services.interfaces;

import com.megacode.models.response.NivelesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NivelService {
    @GET("api/nivel/listarNiveles")
    Call<List<NivelesResponse>> listarNiveles();
}
