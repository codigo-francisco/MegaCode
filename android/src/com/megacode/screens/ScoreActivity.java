package com.megacode.screens;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAfterTransition();
    }
}
