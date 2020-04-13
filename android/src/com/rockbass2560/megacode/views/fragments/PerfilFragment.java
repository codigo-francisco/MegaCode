package com.rockbass2560.megacode.views.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.viewmodels.PerfilViewModel;
import com.rockbass2560.megacode.views.activities.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Locale;

import androidx.lifecycle.ViewModelProviders;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private final static int REQUEST_GET_SINGLE_FILE = 1;
    private final static int REQUEST_CAMERA_PERMISSION = 3;
    private final static String TAG = "PerfilFragment";
    private static final int REQUEST_CAMERA = 2;
    private AppCompatImageButton fotoPerfil, buttonMegaCode, buttonSheMegaCode;
    private PerfilViewModel perfilViewModel;
    private static final int THUMBSIZE = 128;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_GET_SINGLE_FILE:
                    if (data!=null){
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), data.getData());
                            almacenarFotografiaPerfil(bitmap);
                            bitmap.recycle();
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                    break;
                case REQUEST_CAMERA:
                    if (data!=null){
                        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                        almacenarFotografiaPerfil(bitmap);
                        bitmap.recycle();
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION){
            if (Arrays.stream(grantResults).anyMatch( p -> p == PackageManager.PERMISSION_GRANTED )){
                solicitarFotografiaCamara();
            }else{
                Toast.makeText(getContext(), "Permiso de camara no otorgado", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void almacenarFotografiaPerfil(Bitmap bp){
        final Bitmap bitmap = ThumbnailUtils.extractThumbnail(
                bp,
                THUMBSIZE,
                THUMBSIZE
        );

        Glide.with(this)
                .load(getResources().getDrawable(R.drawable.progress_animation))
                .placeholder(getResources().getDrawable(R.drawable.progress_animation))
                .into(fotoPerfil);

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(user.getUid()+"/fotoPerfil.png");

            //Guardar imagen
            storageReference.putBytes(outputStream.toByteArray())
                .addOnCompleteListener(task -> {
                    Glide.with(this)
                            .load(bitmap)
                            .into(fotoPerfil);
                }
            );
        }catch (IOException ex){
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    private void solicitarFotografiaCamara(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)){
                new AlertDialog.Builder(getActivity())
                        .setMessage("Es necesario dar permisos de camara para poder tomar una fotografía de tu perfil")
                        .setCancelable(false)
                        .setPositiveButton("Solicitar permisos de nuevo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                            }
                        })
                        .show();
            }else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        }else{
            Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intentCamara, REQUEST_CAMERA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_perfil, container, false);

        perfilViewModel = ViewModelProviders.of(this).get(PerfilViewModel.class);

        fotoPerfil = fragmentView.findViewById(R.id.foto_perfil);

        perfilViewModel.consultarUsuario().observe(getViewLifecycleOwner(), usuario -> {
            //Se colocan los valores
            if (usuario!=null) {
                ((TextView) fragmentView.findViewById(R.id.name_view)).setText(usuario.nombre);
                ((TextView) fragmentView.findViewById(R.id.text_age)).setText(String.format(Locale.getDefault(), "%d %s",
                        usuario.edad, getResources().getString(R.string.anios)));
                ((TextView) fragmentView.findViewById(R.id.text_sex)).setText(usuario.sexo);

                StorageReference imageReference = FirebaseStorage.getInstance()
                        .getReference(usuario.id+"/fotoPerfil.png");

                imageReference.getDownloadUrl()
                    .addOnSuccessListener(url -> {
                        if (this.getActivity() != null) {
                            //Carga de la imagen
                            Glide.with(this)
                                    .load(url)
                                    .into(fotoPerfil);
                        }
                    });
            }
        });

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
                            solicitarFotografiaCamara();
                    }
                })
                .create();

        fotoPerfil.setOnClickListener(view -> {
            dialog.show();
        });

        Button button = fragmentView.findViewById(R.id.perfil_cerrarsesion);
        button.setOnClickListener(view -> {

            //Cerrar sesión
            FirebaseAuth.getInstance().signOut();

            //Cambiar de actividad con una tarea nueva
            Intent intent = new Intent(this.getActivity(), LoginActivity.class);
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
