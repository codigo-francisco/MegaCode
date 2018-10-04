package com.megacode.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class ActivityBase extends AppCompatActivity {
    protected Toast errorGeneralMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        errorGeneralMessage = Toast.makeText(this, "Ha ocurrido un error en el proceso", Toast.LENGTH_LONG);
    }

}
