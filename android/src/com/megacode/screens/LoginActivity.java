package com.megacode.screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView loginTextRegistrate = findViewById(R.id.login_text_registrate);

        loginTextRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Abrir actividad de registro
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
