package com.megacode.views.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.megacode.GameplayScreen;
import com.megacode.MegaCodeGame;
import com.megacode.models.InfoNivel;

/**
 * Created by Francisco on 22/02/2018.
 */

public class GameFragment extends AndroidFragmentApplication {

    private MegaCodeGame game;

    public GameFragment(InfoNivel infoNivel){
        this.game = new MegaCodeGame(infoNivel);
    }

    public MegaCodeGame getGame() {
        return game;
    }

    public GameplayScreen getGamePlayScreen(){
        return (GameplayScreen)game.getScreen();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return initializeForView(game);
    }
}