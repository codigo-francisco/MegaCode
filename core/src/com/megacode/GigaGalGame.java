package com.megacode;

import com.badlogic.gdx.Game;

public class GigaGalGame extends Game {

    private String rutaNivel;

    public GigaGalGame(String rutaNivel){
        this.rutaNivel = rutaNivel;
    }

    @Override
    public void create() {
        setScreen(new GameplayScreen(rutaNivel));
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
