package com.rockbass2560.megacode.components;

import android.content.Context;
import android.media.MediaPlayer;

import com.rockbass2560.megacode.R;

public class MediaPlayerManager {

    private static MediaPlayerManager INSTANCE;
    public final static int MUSICA_COMANDOS = R.raw.big_birds_date;
    public final static int MUSICA_SI = R.raw.defense_line;
    public final static int MUSICA_PARA = R.raw.no_monkey;
    public final static int MUSICA_MIENTRAS = R.raw.ipsi;
    public final static int ENTRADA = R.raw.highlander;

    private final static float MAX_VOLUME = 0.7f;
    private final static float MIN_VOLUME = 0;

    public static MediaPlayerManager getInstance(Context context){
        if (INSTANCE==null){
            INSTANCE = new MediaPlayerManager(context);
        }

        return INSTANCE;
    }

    private boolean isReleased = true;
    private boolean isSilent;
    private float volume = MAX_VOLUME;
    private MediaPlayer mediaPlayer;
    private Context context;
    private int lastPosition = 0;
    private int musica;
    private boolean isBackActivity = false;

    public void cambiarCancion(int musica){
        this.musica = musica;
        mediaPlayer.release();
        lastPosition = 0;
        crearMediaPlayer();
    }

    public void reiniciarPosicion(){
        lastPosition = 0;
    }

    public void cerrarMediaPlayer(){
        lastPosition = mediaPlayer.getCurrentPosition();
        mediaPlayer.release();
        isReleased = true;
    }

    public void iniciarMediaPlayer(){
        iniciarMediaPlayer(isSilent);
    }

    public void iniciarMediaPlayer(boolean isSilent){
        if (isReleased){
            this.isSilent = isSilent;
            volume = (isSilent) ? MIN_VOLUME : MAX_VOLUME;
            crearMediaPlayer();
            isReleased = false;
        }
        mediaPlayer.start();
    }

    public void toogleVolume(){
        volume = (volume==MAX_VOLUME) ? MIN_VOLUME : MAX_VOLUME;
        mediaPlayer.setVolume(volume, volume);
    }

    public boolean isSilent(){
        return volume == MIN_VOLUME;
    }

    /*public void silentMediaPlayer(){
        volume = 0;
        mediaPlayer.setVolume(volume, volume);
    }

    public void turnOnVolumeMediaPlayer(){
        volume = 1;
        mediaPlayer.setVolume(volume, volume);
    }*/

    private void crearMediaPlayer(){
        mediaPlayer = MediaPlayer.create(context, musica);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(volume,volume);
        mediaPlayer.seekTo(lastPosition);
    }

    private MediaPlayerManager(Context context){
        this.context = context;
        musica = R.raw.highlander;
    }

    public boolean isBackActivity(){
        return isBackActivity;
    }

    public void setBackActivity(boolean backActivity) {
        isBackActivity = backActivity;
    }
}
