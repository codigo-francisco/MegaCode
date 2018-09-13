package com.megacode.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.megacode.services.MegaCodeService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ActivityBase extends AppCompatActivity {
    protected Toast errorGeneralMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        errorGeneralMessage = Toast.makeText(this, "Ha ocurrido un error en el proceso", Toast.LENGTH_LONG);
    }

}
