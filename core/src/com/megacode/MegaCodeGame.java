package com.megacode;

import com.badlogic.gdx.Game;
import com.megacode.models.InfoNivel;

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
}
