package com.rockbass2560.megacode.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.rockbass2560.megacode.R;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAfterTransition();
    }
}
