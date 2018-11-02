package com.megacode.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.moshi.MoshiConverterFactory;

public class MegaCodeService {

    private MegaCodeService(){}

    private Map servicios = new HashMap<>();

    private OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.83/megacode/")
            //.addConverterFactory(MoshiConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

    private static MegaCodeService INSTANCE;

    public static <T> T getServicio(Class<T> typeService){
        if (INSTANCE==null){
            INSTANCE = new MegaCodeService();
        }

        T servicio;

        if (INSTANCE.servicios.containsKey(typeService)){
            servicio = (T) INSTANCE.servicios.get(typeService);
        }else{
            servicio = INSTANCE.retrofit.create(typeService);
            INSTANCE.servicios.put(typeService, servicio);
        }

        return servicio;
    }

}
