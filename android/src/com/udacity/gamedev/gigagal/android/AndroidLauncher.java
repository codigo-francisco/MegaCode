package com.udacity.gamedev.gigagal.android;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.udacity.gamedev.gigagal.GigaGalGame;

public class AndroidLauncher extends AppCompatActivity implements  AndroidFragmentApplication.Callbacks{ //extends AndroidApplication {

	public final static String TAG = "Launcher";

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BlocklyFragment blocklyFragment = new BlocklyFragment();

		// Create libgdx fragment
		GameFragment libgdxFragment = new GameFragment();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		transaction
				.add(R.id.content_blockly, blocklyFragment)
				.add(R.id.content_framelayout, libgdxFragment)
				.commit();
	}

	@Override
	public void exit() {
		Log.d(TAG, "exit");
	}
}
