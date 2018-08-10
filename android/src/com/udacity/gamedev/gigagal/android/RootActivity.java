package com.udacity.gamedev.gigagal.android;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class RootActivity extends AppCompatActivity {

    private Toolbar toolbarMenu;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private PerfilFragment perfilFragment;
    private Persona persona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        drawerLayout = findViewById(R.id.drawer_layout);

        toolbarMenu = findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbarMenu);

        navigationView = findViewById(R.id.menu_navigation);

        //Instancia de opciones
        perfilFragment = new PerfilFragment();

        //Persona Dummy
        persona = new Persona(29, "Francisco Gonzalez", "Masculino");

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        //item.setChecked(true);

                        FragmentManager manager = getSupportFragmentManager();

                        //Aquí se hace el cambio de fragmento
                        int id = item.getItemId();
                        switch (id){
                            case R.id.perfil:
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("persona", persona);
                                perfilFragment.setArguments(bundle);
                                manager.beginTransaction()
                                        .replace(R.id.content_frame, perfilFragment)
                                        .commit();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), R.string.opcion_no_implementada, Toast.LENGTH_SHORT).show();
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                }
        );

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbarMenu,R.string.abierto, R.string.cerrado);
        actionBarDrawerToggle.syncState();
    }
}
