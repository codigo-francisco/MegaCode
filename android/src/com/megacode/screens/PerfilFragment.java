package com.megacode.screens;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageButton;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.megacode.models.Persona;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private final static int REQUEST_GET_SINGLE_FILE = 1;
    private final static int RESULT_OK = -1;
    private final static String TAG = "PerfilFragment";
    private static final int REQUEST_CAMERA = 2;
    private AppCompatImageButton fotoPerfil, buttonMegaCode, buttonSheMegaCode;

    public PerfilFragment() {
        // Required empty public constructor
    }

    private static class CargarImagen extends AsyncTask<Uri, Void, String>{

        private Context context;

        private CargarImagen(Context context){
            this.context = context;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Codigo de reto :P

            /*Persona persona = null;

            //Mandar la imagen a base de datos
            try(Realm realm = Realm.getDefaultInstance()){
                persona = realm.where(Persona.class).findFirst().buildPersonaObj();
            }

            MegaCodeServiceInstance.getMegaCodeServiceInstance().
                    megaCodeService.registrarFotoUsuario(persona.getToken(), persona).clone().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Guardado en base de datos", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Guardado en base de datos, codigo:"+response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "Hubo un error al guardar", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Hubo un error al guardar");
                }
            });*/
        }

        public static String encodeTobase64(Bitmap image) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

            Log.d(TAG, imageEncoded);
            return imageEncoded;
        }

        @Override
        protected String doInBackground(Uri... uris) {
            String result=null;

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uris[0]));
                result = encodeTobase64(bitmap);

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();

                Persona persona = realm.where(Persona.class).findFirst();
                persona.setFotoPerfil(result);
                realm.copyToRealmOrUpdate(persona);

                realm.commitTransaction();

                realm.close();
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
            switch (requestCode){
                case REQUEST_GET_SINGLE_FILE:
                    if (data!=null){
                        fotoPerfil.setImageURI(data.getData());
                        fotoPerfil.setBackgroundResource(R.color.translucent);

                        new CargarImagen(getContext()).execute(data.getData());
                    }
                    break;
                case REQUEST_CAMERA:
                    if (data!=null){
                        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                        Glide.with(getContext()).load(bitmap).into(fotoPerfil);
                        //fotoPerfil.setImageBitmap(bitmap);
                        try {
                            File file = File.createTempFile("fotoPerfil","png");
                            try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                                if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)){
                                    new CargarImagen(getContext()).execute(Uri.fromFile(file));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_perfil, container, false);

        Realm realm = Realm.getDefaultInstance();
        Persona persona = realm.where(Persona.class).findFirst();

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

        realm.close();

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Escoga una opción")
                        .setItems(new String[]{
                                "Dispositivo",
                                "Camara"
                        }, (dialogInterface, index) -> {
                            switch (index){
                                case 0:
                                    Intent intentDispositivo = new Intent(Intent.ACTION_GET_CONTENT);
                                    intentDispositivo.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intentDispositivo, "Selecciona una imagen"), REQUEST_GET_SINGLE_FILE);
                                    break;
                                case 1:
                                    Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intentCamara, REQUEST_CAMERA);
                            }
                        })
                        .create();

                dialog.show();
            }
        });

        Button button = fragmentView.findViewById(R.id.perfil_cerrarsesion);
        button.setOnClickListener(view -> {
            //Borrar todos los usuarios en Realm
            try(Realm realm1 = Realm.getDefaultInstance()){
                realm1.beginTransaction();
                realm1.where(Persona.class).findFirst().deleteFromRealm();
                realm1.commitTransaction();

                //Cambiar de actividad con una tarea nueva
                Intent intent = new Intent(PerfilFragment.this.getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        buttonMegaCode = fragmentView.findViewById(R.id.button_megacode);
        buttonMegaCode.setOnClickListener(clickListener);
        buttonSheMegaCode = fragmentView.findViewById(R.id.button_shemegacode);
        buttonSheMegaCode.setOnClickListener(clickListener);

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
