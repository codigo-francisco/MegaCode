package com.megacode.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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

    public static MegaCodeService megaCodeService = retrofit.create(MegaCodeService.class);
    protected Toast errorGeneralMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        errorGeneralMessage = Toast.makeText(this, "Ha ocurrido un error en el proceso", Toast.LENGTH_LONG);
    }

}
