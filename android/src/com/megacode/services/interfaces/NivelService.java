package com.megacode.services.interfaces;

import com.megacode.models.database.Nivel;
import com.megacode.models.response.ListarNivelesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface NivelService {
    @GET("api/nivel/listarNiveles")
    Call<List<Nivel>> listarNiveles();

    @GET("api/nivel/listarNiveles/{id}")
    Call<ListarNivelesResponse> listarNiveles(@Header("Authorization") String token, @Path("id") long id);
}
