package com.rockbass2560.megacode.views.activities;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import com.rockbass2560.megacode.Claves;
import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.models.database.Usuario;
import com.rockbass2560.megacode.viewmodels.RootViewModel;
import com.rockbass2560.megacode.views.fragments.FeedFragment;
import com.rockbass2560.megacode.views.fragments.PerfilFragment;
import com.rockbass2560.megacode.views.fragments.ProgresoFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.preference.PreferenceManager;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.Toast;

public class RootActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private int selectedFragment;
    private final static String SELECTED_FRAGMENT = "selectedFragment";
    private int RESULT_GAME = 1;
    private NavigationView navigationView;
    private Toolbar toolbarMenu;
    private RootViewModel rootViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        rootViewModel = ViewModelProviders.of(this).get(RootViewModel.class);
        rootViewModel.obtenerUsuario().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if (usuario != null) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RootActivity.this);
                    sharedPreferences.edit().putLong(Claves.ID_USUARIO, usuario.getId()).apply();
                }
            }
        });

        toolbarMenu = findViewById(R.id.toolbar_main);
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
        //boolean isFragment = id!=selectedFragment && id!=R.id.jugar;
        boolean isFragment = tags.indexOfKey(id)>-1;

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
                        toolbarMenu.setTitle("Feed");
                        fragment = new FeedFragment();
                        break;
                    case R.id.perfil:
                        toolbarMenu.setTitle("Perfil");
                        fragment = new PerfilFragment();
                        break;
                    case R.id.progreso:
                        toolbarMenu.setTitle("Progreso");
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
        }else{ //Actividad
            Intent intent = null;

            /*switch(id){
                case R.id.jugar:
                    intent = new Intent(this, MegaCodeAcitivity.class);
                    break;
            }*/

            if (intent!=null){
                result=true;
                intent.putExtra("selectedFragment", selectedFragment);
                startActivityForResult(intent, RESULT_GAME);
            }
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
        } else if (requestCode==Claves.ABRIR_NIVEL_MEGACODE) {
            if (resultCode == Activity.RESULT_OK){
                getSupportFragmentManager().findFragmentByTag(tags.get(R.id.progreso)).onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.