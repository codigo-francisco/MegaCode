package com.megacode.screens;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.megacode.models.Persona;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import io.realm.Realm;

import static com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShape.line;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private final static int REQUEST_GET_SINGLE_FILE = 1;
    private final static int RESULT_OK = -1;
    private final static String TAG = "PerfilFragment";
    private AppCompatImageButton fotoPerfil, buttonMegaCode, buttonSheMegaCode;
    private Persona persona;

    public PerfilFragment() {
        // Required empty public constructor
    }

    private static class CargarImagen extends AsyncTask<Uri, Void, String>{

        private ContentResolver contentResolver;
        private Persona persona;

        public CargarImagen(ContentResolver contentResolver, Persona persona){
            this.contentResolver = contentResolver;
            this.persona = persona;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Uri... uris) {
            String result=null;

            try {
                InputStreamReader r = new InputStreamReader(contentResolver.openInputStream(uris[0]));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int bit;
                while ((bit = r.read()) != -1) {
                    byteArrayOutputStream.write(bit);
                }
                result = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                persona.setFotoPerfil(result);

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(persona);
                realm.commitTransaction();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return result;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            if (requestCode == REQUEST_GET_SINGLE_FILE){
                if (data!=null){
                    fotoPerfil.setImageURI(data.getData());
                    fotoPerfil.setBackgroundResource(R.color.translucent);

                    new CargarImagen(getContext().getContentResolver(), persona).execute(data.getData());
                }
            }
        }
    }

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_perfil, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        try {
            persona = Persona.buildPersonaFromJson(preferences.getString(getString(R.string.persona),null));

            //Se colocan los valores
            ((TextView)fragmentView.findViewById(R.id.name_view)).setText(persona.getNombre());
            ((TextView)fragmentView.findViewById(R.id.text_age)).setText(String.format(Locale.getDefault(),"%d %s",persona.getEdad(), getResources().getString(R.string.anios)));
            ((TextView)fragmentView.findViewById(R.id.text_sex)).setText(persona.getSexo());

            fotoPerfil = fragmentView.findViewById(R.id.foto_perfil);
            //Cargar imagen
            if (persona.getFotoPerfil()!=null) {
                byte[] bytes = Base64.decode(persona.getFotoPerfil(), Base64.DEFAULT);
                fotoPerfil.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }

            fotoPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), REQUEST_GET_SINGLE_FILE);
                }
            });

            buttonMegaCode = fragmentView.findViewById(R.id.button_megacode);
            buttonMegaCode.setOnClickListener(clickListener);
            buttonSheMegaCode = fragmentView.findViewById(R.id.button_shemegacode);
            buttonSheMegaCode.setOnClickListener(clickListener);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return fragmentView;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.button_megacode:
                    buttonSheMegaCode.setBackgroundResource(0);
                    buttonMegaCode.setBackgroundResource(R.drawable.borders);
                    break;
                case R.id.button_shemegacode:
                    buttonMegaCode.setBackgroundResource(0);
                    buttonSheMegaCode.setBackgroundResource(R.drawable.borders);
                    break;
            }
        }
    };
}
