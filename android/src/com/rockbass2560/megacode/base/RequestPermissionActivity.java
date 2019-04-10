package com.rockbass2560.megacode.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.telecom.Call;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public abstract class RequestPermissionActivity extends AppCompatActivity {

    private static int CAMERA_REQUEST_PERMISSION = 101, WRITE_STORAGE_PERMISSION = 100;

    public static interface CallbackGranted{
        void granted();
        void denied();
    }

    private CallbackGranted cameraGranted, writeStorageGranted;

    public RequestPermissionActivity(CallbackGranted cameraGranted, CallbackGranted writeStorageGranted){
        this.cameraGranted = cameraGranted;
        this.writeStorageGranted = writeStorageGranted;
    }

    public void addCameraGrantedListener(CallbackGranted cameraGranted){
        this.cameraGranted = cameraGranted;
    }

    public void addWriteStorageListener(CallbackGranted writeStorageGranted){
        this.writeStorageGranted = writeStorageGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST_PERMISSION){
            if (grantResults.length == 0){
                if (cameraGranted!=null)
                    cameraGranted.denied();

            }else{
                if (cameraGranted!=null){
                    cameraGranted.granted();
                }
            }
        }else if (requestCode == WRITE_STORAGE_PERMISSION){
            if (grantResults.length == 0){
                requestPermissionWriteStorage();
            }
        }
    }

    public void requestPermissionWriteStorage(){
        final String titulo = "Otorgar permisos de escritura";
        final int codeRequest = WRITE_STORAGE_PERMISSION;
        final String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        final String explicacion = "MegaCode necesita almacenar datos en tu almacenamiento externo para sincronizar las imagenes obtenidas con el servidor.\n"+
                "Estos archivos se utiilizan con fines cientificos y no se publicaran en ningún sitio";
        final String tituloExplicacion = "Solicitar permiso de nuevo";

        requestPermission(permission, titulo, codeRequest, tituloExplicacion, explicacion);
    }

    public void requestPermissionCamera(){
        final String titulo = "Otorgar permisos de la camara";
        final int codeRequest = CAMERA_REQUEST_PERMISSION;
        final String permission = Manifest.permission.CAMERA;
        final String explicacion = "MegaCode toma fotografía de tu rostros mientras trabajas con el sistema por lo que es necesario utilizar la camara del dispositivo.\n" +
                                        "Estas fotografías se utilizan nada más con fines cientificos y no se publicaran en ningún sitio.";

        final String tituloExplicacion = "Solicitar permiso de nuevo";

        requestPermission(permission, titulo, codeRequest, tituloExplicacion, explicacion);
    }

    private void requestPermission(String permission, String titulo, int codeRequest, String tituloExplicacion, String explicacion){
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                        .setTitle(titulo)
                        .setMessage(explicacion)
                        .setCancelable(false)
                        .setPositiveButton(tituloExplicacion, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == AlertDialog.BUTTON_POSITIVE) {
                                    ActivityCompat.requestPermissions(RequestPermissionActivity.this,
                                            new String[]{permission},
                                            codeRequest);
                                }
                            }
                        });

                dialog.show();
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        codeRequest);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        if (cameraGranted != null)
            requestPermissionCamera();
        if (writeStorageGranted != null)
            requestPermissionWriteStorage();
    }
}
