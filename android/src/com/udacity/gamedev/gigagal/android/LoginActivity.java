package com.udacity.gamedev.gigagal.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MaterialBetterSpinner spinner = findViewById(R.id.spinner_sexo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.sexos,android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
    }
}
