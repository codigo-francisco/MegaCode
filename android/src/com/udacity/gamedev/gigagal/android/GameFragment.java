package com.udacity.gamedev.gigagal.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.udacity.gamedev.gigagal.GameplayScreen;
import com.udacity.gamedev.gigagal.GigaGalGame;
import com.udacity.gamedev.gigagal.Level;

/**
 * Created by Francisco on 22/02/2018.
 */

public class GameFragment extends AndroidFragmentApplication {

    public static Game GAME;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GigaGalGame game = new GigaGalGame();
        GAME = game;
        return initializeForView(game);
    }
}