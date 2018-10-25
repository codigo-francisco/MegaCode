package com.megacode.views.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.megacode.R;
import com.megacode.helpers.ImageProfileHelper;
import com.megacode.models.database.Usuario;
import com.megacode.viewmodels.UsuarioViewModel;
import com.megacode.views.activities.LoginActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import androidx.lifecycle.ViewModelProviders;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private final static int REQUEST_GET_SINGLE_FILE = 1;
    private final static int REQUEST_CAMERA_PERMISSION = 3;
    private final static int RESULT_OK = -1;
    private final static String TAG = "PerfilFragment";
    private static final int REQUEST_CAMERA = 2;
    private AppCompatImageButton fotoPerfil, buttonMegaCode, buttonSheMegaCode;

    public PerfilFragment() {
        // Required empty public constructor
    }

    private class CargarImagen extends AsyncTask<Uri, Void, String>{

        private ContentResolver contentResolver;

        private CargarImagen(ContentResolver contentResolver){
            this.contentResolver = contentResolver;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Uri... uris) {
            String result=null;

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uris[0]));
                result = ImageProfileHelper.encodeTobase64(bitmap);

                Usuario usuario = usuarioViewModel.obtenerUsuario().getValue();

                usuario.setFotoPerfil(result);

                usuarioViewModel.update(usuario);
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

                        new CargarImagen(getContext().getContentResolver()).execute(data.getData());
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
                                    new CargarImagen(getContext().getContentResolver()).execute(Uri.fromFile(file));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CAMERA_PERMISSION:
                    solicitarFotografiaCamara();
                    break;
            }
        }
    }

    private UsuarioViewModel usuarioViewModel;

    private void solicitarFotografiaCamara(){
        Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentCamara, REQUEST_CAMERA);
    }

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_perfil, container, false);

        usuarioViewModel = ViewModelProviders.of(this).get(UsuarioViewModel.class);

        usuarioViewModel.obtenerUsuario().observe(this, usuario -> {
            //Se colocan los valores
            ((TextView)fragmentView.findViewById(R.id.name_view)).setText(usuario.getNombre());
            ((TextView)fragmentView.findViewById(R.id.text_age)).setText(String.format(Locale.getDefault(),"%d %s",
                    usuario.getEdad(), getResources().getString(R.string.anios)));
            ((TextView)fragmentView.findViewById(R.id.text_sex)).setText(usuario.getSexo());

            fotoPerfil = fragmentView.findViewById(R.id.foto_perfil);

            //Cargar imagen
            if (usuario.getFotoPerfil()!=null) {
                byte[] bytes = Base64.decode(usuario.getFotoPerfil(), Base64.DEFAULT);
                fotoPerfil.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });

        fotoPerfil.setOnClickListener(view -> {
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
                                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{ Manifest.permission.CAMERA }, REQUEST_CAMERA_PERMISSION);
                                }else{
                                    solicitarFotografiaCamara();
                                }
                        }
                    })
                    .create();

            dialog.show();
        });

        Button button = fragmentView.findViewById(R.id.perfil_cerrarsesion);
        button.setOnClickListener(view -> {
            usuarioViewModel.borrarUsuario();
            //Cambiar de actividad con una tarea nueva
            Intent intent = new Intent(PerfilFragment.this.getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
