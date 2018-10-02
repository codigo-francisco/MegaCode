package com.megacode.screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.Toast;

import com.megacode.models.Persona;
import com.megacode.models.Tags;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class RootActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private int selectedFragment;
    private final static String SELECTED_FRAGMENT = "selectedFragment";
    private int RESULT_GAME = 1;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        Toolbar toolbarMenu = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbarMenu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.menu_navigation);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbarMenu,R.string.abierto, R.string.cerrado);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        int selectedFragment;

        if (savedInstanceState!=null){
            selectedFragment = savedInstanceState.getInt(SELECTED_FRAGMENT, R.id.feed);
        }else{
            selectedFragment = R.id.feed;
        }

        navigationView.setCheckedItem(selectedFragment);
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
        return selectFragment(menuItem.getItemId());
    }

    private final static SparseArray<String> tags = new SparseArray<>();
    static {
        tags.append(R.id.feed, "FEED");
        tags.append(R.id.perfil, "PERFIL");
        tags.append(R.id.progreso, "PROGRESO");
    }

    public boolean selectFragment(int id){

        boolean result=false;
        boolean isFragment = id!=selectedFragment && id!=R.id.jugar;

        if (isFragment) {
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = null;
            selectedFragment = id;
            String currentTag = tags.get(id);
            boolean iguales = (manager.getFragments().size() > 0) && (manager.getFragments().get(0).getTag().equals(currentTag));

            if (!iguales) {
                //Aquí se hace el cambio de fragmento
                switch (id) {
                    case R.id.feed:
                        fragment = new FeedFragment();
                        break;
                    case R.id.perfil:
                        fragment = new PerfilFragment();
                        break;
                    case R.id.progreso:
                        fragment = new ProgresoFragment();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), R.string.opcion_no_implementada, Toast.LENGTH_SHORT).show();
                }

                if (fragment != null) {
                    manager.beginTransaction()
                            .replace(R.id.frame_layout, fragment, currentTag)
                            .commitNow();
                }
            }

            result=true;
        }else if (id==R.id.jugar){
            //Intent intent = new Intent(this, MegaCodeAcitivity.class);
            Intent intent = new Intent(this, OpenCVDownload.class);
            intent.putExtra("selectedFragment", selectedFragment);
            //startActivityForResult(intent, RESULT_GAME);
            startActivity(intent);

            result=true;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RESULT_GAME){
            if (resultCode==RESULT_OK){
                if (data!=null){
                    data.getExtras().getInt("selectedFragment");
                    navigationView.setCheckedItem(selectedFragment);
                    selectFragment(selectedFragment);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_FRAGMENT, selectedFragment);

        super.onSaveInstanceState(outState);
    }
}
