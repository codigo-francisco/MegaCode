package com.rockbass2560.megacode;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.rockbass2560.megacode.models.InfoNivel;

public class MegaCodeGame extends Game {

    private InfoNivel infoNivel;

    public MegaCodeGame(InfoNivel infoNivel){
        this.infoNivel = infoNivel;
    }

    @Override
    public void create() {
        setScreen(new GameplayScreen(infoNivel));
        if (gameListener!=null){
            gameListener.loadedGame();
        }
    }

    public void addLoadGameListener(LoadGameListener listener){
        gameListener = listener;
    }

    private LoadGameListener gameListener;

    public interface LoadGameListener{
        void loadedGame();
    }

    @Override
    public void dispose() {
        super.dispose();
        screen.dispose();
    }
}
