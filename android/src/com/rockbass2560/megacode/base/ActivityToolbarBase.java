package com.rockbass2560.megacode.base;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;

import com.rockbass2560.megacode.Claves;
import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.components.MediaPlayerManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

public abstract class ActivityToolbarBase extends AppCompatActivity {

    protected int resourceBase;
    protected Toolbar toolbarMenu;
    protected MediaPlayerManager mediaPlayerManager;
    protected MediaPlayer mediaPlayerSoundClick;
    private SharedPreferences sharedPreferences;

    public ActivityToolbarBase(int resourceBase){
        this.resourceBase = resourceBase;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(resourceBase);

        toolbarMenu = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbarMenu);

        sharedPreferences = getSharedPreferences(Claves.SHARED_MEGACODE_PREFERENCES,0);

        mediaPlayerManager = MediaPlayerManager.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar_general, menu);

        MenuItem menuItem = menu.findItem(R.id.button_sound);
        changeVolumeIcon(menuItem);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.button_sound){
            int idIcon = 0;

            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (mediaPlayerManager.isSilent()){
                idIcon = R.drawable.music_on;
                editor.putBoolean(Claves.MUSIC_VOLUME, false);
            }else{
                idIcon = R.drawable.music_off;
                editor.putBoolean(Claves.MUSIC_VOLUME, true);
            }

            editor.apply();
            item.setIcon(idIcon);
            mediaPlayerManager.toogleVolume();
        }
        return true;
    }

    protected void changeVolumeIcon(){
        MenuItem menuItem = toolbarMenu.getMenu().findItem(R.id.button_sound);
        if (menuItem!=null){
            changeVolumeIcon(menuItem);
        }
    }

    protected void changeVolumeIcon(MenuItem item){
        int idIcon = 0;

        boolean isSilent = sharedPreferences.getBoolean(Claves.MUSIC_VOLUME, false);

        if (isSilent){
            idIcon = R.drawable.music_off;
        }else{
            idIcon = R.drawable.music_on;
        }

        item.setIcon(idIcon);
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isSilent = sharedPreferences.getBoolean(Claves.MUSIC_VOLUME, false);

        mediaPlayerManager.iniciarMediaPlayer(isSilent);

        mediaPlayerSoundClick = MediaPlayer.create(this, R.raw.button_click_dialog);
        mediaPlayerSoundClick.setVolume(1,1);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sharedPreferences.edit().putBoolean(Claves.MUSIC_VOLUME, mediaPlayerManager.isSilent()).apply();

        mediaPlayerManager.cerrarMediaPlayer();

        mediaPlayerSoundClick.release();
        mediaPlayerSoundClick = null;
    }
}
