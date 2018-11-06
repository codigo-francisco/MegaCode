package com.megacode.services.interfaces;

import com.megacode.models.response.ScoreResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ScoreService {
    @GET("api/score")
    Call<List<ScoreResponse>> puntajes();
}
