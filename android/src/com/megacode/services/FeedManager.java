package com.megacode.services;

import android.util.Log;

import com.megacode.adapters.model.DataModel;
import com.megacode.base.ActivityBase;
import com.megacode.models.response.PosicionesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedManager {

    private final static String TAG = "FeedManager";

    public static List<DataModel> siguienteEjercicio(){
        List<DataModel> dataModels = new ArrayList<>();

        return dataModels;
    }

    public static void posicionContraOtros(String email){
        /*ActivityBase.megaCodeService.posiconContraOtros(email).clone().enqueue(
                new Callback<List<PosicionesResponse>>() {
                    @Override
                    public void onResponse(Call<List<PosicionesResponse>> call, Response<List<PosicionesResponse>> response) {
                        if (response.isSuccessful()){

                        }
                    }

                    @Override
                    public void onFailure(Call<List<PosicionesResponse>> call, Throwable t) {
                        Log.e(TAG, t.getMessage(), t);
                    }
                }
        );*/
    }
}
