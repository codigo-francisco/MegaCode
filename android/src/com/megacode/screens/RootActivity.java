package com.megacode.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.megacode.models.Persona;
import com.megacode.models.Tags;

import java.util.EnumMap;

public class RootActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbarMenu;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private int selectedFragment;
    private SharedPreferences sharedPreferences;
    private String currentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        toolbarMenu = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbarMenu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.menu_navigation);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbarMenu,R.string.abierto, R.string.cerrado);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        selectedFragment = sharedPreferences.getInt("selectedFragment", R.id.feed);

        //Cargar el perfil por default
        selectFragment(selectedFragment);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        return selectFragment(menuItem.getItemId());
    }

    public boolean selectFragment(int id){

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment=null;
        String tag = null;
        selectedFragment = id;

        //Aquí se hace el cambio de fragmento
        switch (id){
            case R.id.feed:
                fragment = new FeedFragment();
                tag = Tags.FEED.toString();
                break;
            case R.id.perfil:
                fragment = new PerfilFragment();
                tag = Tags.PERFIL.toString();
                break;
            case R.id.progreso:
                fragment = new ProgresoFragment();
                tag = Tags.PROGRESO.toString();
                break;
            case R.id.jugar:
                Intent intent = new Intent(this, MegaCodeAcitivity.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(getApplicationContext(), R.string.opcion_no_implementada, Toast.LENGTH_SHORT).show();

        }

        currentTag = tag;

        if (fragment!=null) {
            fragment.setRetainInstance(true);
            manager.beginTransaction()
                    .replace(R.id.frame_layout, fragment, tag)
                    .commitNow();
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedFragment", selectedFragment);
        editor.commit();
    }
}
