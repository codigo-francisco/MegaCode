package com.megacode.services.interfaces;

import com.megacode.models.response.NivelResponse;
import com.megacode.models.response.PosicionesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface FeedService {

    @GET("api/feed/posicionContraOtros/{id}")
    Call<List<PosicionesResponse>> posicionContraOtros(@Header("Authorization") String token, @Path("id") long id);

    @GET("api/feed/siguienteEjercicio/{id}")
    Call<NivelResponse> siguienteEjercicio(@Header("Authorization") String token, @Path("id") long id);

}
