package com.megacode.services;

import com.megacode.models.database.Usuario;
import com.megacode.models.RegistroResponse;
import com.megacode.models.ScoreResponse;
import com.megacode.models.response.LoginResponse;
import com.megacode.models.response.NivelResponse;
import com.megacode.models.response.NivelesResponse;
import com.megacode.models.response.PosicionesResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MegaCodeService {

    @POST("api/autenticacion/registrar")
    Call<RegistroResponse> registrar(@Body Usuario usuario);

    @POST("api/autenticacion/login")
    Call<LoginResponse> login(@Body Usuario usuario);

    @POST("api/usuario/registrarFoto")
    Call<ResponseBody> registrarFotoUsuario(@Header("Authorization") String token, @Body Usuario usuario);

    @GET("api/feed/posicionContraOtros/{id}")
    Call<List<PosicionesResponse>> posicionContraOtros(@Header("Authorization") String token, @Path("id") long id);

    @GET("api/feed/siguienteEjercicio/{id}")
    Call<NivelResponse> siguienteEjercicio(@Header("Authorization") String token, @Path("id") long id);

    @GET("api/score")
    Call<List<ScoreResponse>> puntajes();

    @GET("api/nivel/listarNiveles")
    Call<List<NivelesResponse>> listarNiveles();

}
