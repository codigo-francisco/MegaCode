package com.rockbass2560.megacode.views.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.viewmodels.LoginViewModel;

public class LaunchActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        LoginViewModel loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginViewModel.observadorUsuario().observe(this, firebaseUser -> {
            if (firebaseUser != null){
                Intent intent = new Intent(this,RootActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else{
                Intent intent = new Intent(this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        loginViewModel.validarUsuario();
    }
}
