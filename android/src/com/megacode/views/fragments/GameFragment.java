package com.megacode.views.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.udacity.gamedev.gigagal.GigaGalGame;

/**
 * Created by Francisco on 22/02/2018.
 */

public class GameFragment extends AndroidFragmentApplication {

    public static GigaGalGame GAME;
    private String rutaNivel;

    public GameFragment(String rutaNivel){
        this.rutaNivel = rutaNivel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GigaGalGame game = new GigaGalGame(rutaNivel);
        GAME = game;
        return initializeForView(game);
    }
}