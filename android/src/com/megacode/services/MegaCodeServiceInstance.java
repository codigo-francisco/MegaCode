package com.megacode.services;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MegaCodeServiceInstance {

    private OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.83/megacode/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build();

    public MegaCodeService megaCodeService = retrofit.create(MegaCodeService.class);

    private static MegaCodeServiceInstance megaCodeServiceInstance;

    public static MegaCodeServiceInstance getMegaCodeServiceInstance() {
        if (megaCodeServiceInstance==null){
            megaCodeServiceInstance = new MegaCodeServiceInstance();
        }

        return megaCodeServiceInstance;
    }

    private MegaCodeServiceInstance(){

    }
}
