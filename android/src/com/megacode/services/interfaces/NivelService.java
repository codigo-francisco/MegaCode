package com.megacode.services.interfaces;

import com.megacode.models.database.Nivel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NivelService {
    @GET("api/nivel/listarNiveles")
    Call<List<Nivel>> listarNiveles();
}
