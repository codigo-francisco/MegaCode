package com.megacode.base;

import android.support.v7.app.AppCompatActivity;

import com.megacode.services.MegaCodeService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ActivityBase extends AppCompatActivity {
    protected final static OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(10, TimeUnit.SECONDS).
            readTimeout(10, TimeUnit.SECONDS)
            .build();
    protected final static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.83/megacode/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build();

    protected MegaCodeService megaCodeService = retrofit.create(MegaCodeService.class);

}
