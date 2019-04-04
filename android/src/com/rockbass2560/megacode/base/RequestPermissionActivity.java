package com.rockbass2560.megacode.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public abstract class RequestPermissionActivity extends Activity {

    private static int CAMERA_REQUEST_PERMISSION = 100, WRITE_STORAGE_PERMISSION = 100;

    public static interface CallbackGranted{
        void granted();
        void denied();
    }

    private CallbackGranted cameraGranted, writeStorageGranted;

    public RequestPermissionActivity(CallbackGranted cameraGranted, CallbackGranted writeStorageGranted){
        this.cameraGranted = cameraGranted;
        this.writeStorageGranted = writeStorageGranted;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                        .setTitle("Otorgar permisos de la camara")
                        .setMessage("MegaCode toma fotografía de tu rostros mientras trabajas con el sistema por lo que es necesario utilizar la camara del dispositivo.\n" +
                                "Estas fotografías se utilizan nada más con fines cientificos y no se publicaran en ningún sitio.")
                        .setPositiveButton("Solicitar permiso de nuevo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == AlertDialog.BUTTON_POSITIVE){

                                }
                            }
                        });

                dialog.show();
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST_PERMISSION);
            }

        }

    }
}
