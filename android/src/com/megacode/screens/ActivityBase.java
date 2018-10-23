package com.megacode.screens;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public abstract class ActivityBase extends AppCompatActivity {

    protected Toast errorGeneralMessage;
    protected int selectedFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        errorGeneralMessage = Toast.makeText(this, "Ha ocurrido un error en el proceso", Toast.LENGTH_LONG);

        if (getIntent()!=null && getIntent().getExtras()!=null){
            selectedFragment = getIntent().getIntExtra("selectedFragment",R.id.feed);
        }else{
            selectedFragment = R.id.feed;
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("selectedFragment", selectedFragment);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
