package com.megacode.views.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.megacode.R;

import java.io.File;
import java.util.Locale;

public class OpenCVDownload extends AppCompatActivity {

    private final static String TAG = "OpenCVDownload";
    private static final int REQUEST_CAMERA = 2;
    private static final String DESCARGA_INDEX = "DESCARGA_INDEX";

    private long downloadReference;
    private ProgressBar progressBar;
    private DownloadManager downloadManager;
    private boolean changeProgress;
    private final static int REQUEST_INSTALL_PACKAGE = 1;
    private BroadcastReceiver broadcastReceiverInstallApp;
    private IntentFilter intentFilterInstall;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode)
        {
            case REQUEST_INSTALL_PACKAGE:
                if (grantResults.length>0){
                    botonPermisoInstalacion();
                }else{
                    //Permiso rechazado
                }
                break;
            case REQUEST_CAMERA:
                if (grantResults.length>0){
                    //Prueba de la camara
                    //pruebaDeOpenCV();
                }
                break;
        }
    }

    private void botonPermisoInstalacion(){
        Button buttonInstalar = findViewById(R.id.opencv_instalar);
        Button buttonPermisos = findViewById(R.id.opencv_boton_permisos);
        TextView textView = findViewById(R.id.opencv_mensaje_permiso);

        textView.setVisibility(TextView.GONE);
        buttonPermisos.setVisibility(TextView.GONE);
        buttonInstalar.setEnabled(true);
    }

    private boolean pruebaDeOpenCV(){
        boolean result = false;
        try{
            Log.d(TAG, this.getApplicationInfo().nativeLibraryDir);

            Log.d(TAG, "Existe: "+ new File(this.getApplicationInfo().nativeLibraryDir).exists());

            System.loadLibrary("opencv_java3");

            //si OpenCV cargo correctamente se pasa a la actividad de muestra
            Log.d(TAG,"OpenCV cargado correctamente");

            result = true;
        }catch(UnsatisfiedLinkError ex){
            Log.e(TAG, ex.getMessage(), ex);
        }

        return result;
    }

    private int descargaIndex = 0;

    private String getCurrentAbi(){
        return Build.SUPPORTED_ABIS[descargaIndex];
    }

    private String getCurrentFileName(){
        return String.format(Locale.getDefault(), "OpenCV_Manager_%s.apk", getCurrentAbi());
    }

    private void iniciarDescarga(){
        if (descargaIndex < Build.SUPPORTED_ABIS.length) {
            downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            Uri uri = Uri.parse("http://192.168.1.83/megacode/api/archivos/" + getCurrentAbi());
            DownloadManager.Request request = new DownloadManager.Request(uri);

            request.setTitle("Descargando OpenCV Manager");
            request.setDescription("Se está descargando OpenCV Manager, al finalizar le solicitara que instale el APK");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, getCurrentFileName());

            downloadReference = downloadManager.enqueue(request);
            progressBar.setVisibility(ProgressBar.VISIBLE);

            Thread thread = new Thread(() -> {
                boolean download = true;
                changeProgress = false;

                while (download) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadReference);
                    try (Cursor cursor = downloadManager.query(query)) {
                        if (cursor.moveToFirst()) {
                            int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                            int status = cursor.getInt(statusIndex);

                            int reasonIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                            int reason = cursor.getInt(reasonIndex);

                            int totalBytesIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                            int totalBytes = cursor.getInt(totalBytesIndex);

                            int downloadBytesIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                            int downloadBytes = cursor.getInt(downloadBytesIndex);

                            switch (status) {
                                case DownloadManager.STATUS_RUNNING:
                                    if (totalBytes > -1 && !changeProgress) {
                                        runOnUiThread(() -> {
                                            changeProgress = true;
                                            progressBar.setMax(totalBytes);
                                            progressBar.setIndeterminate(false);
                                        });
                                    } else if (changeProgress) {
                                        runOnUiThread(() -> progressBar.setProgress(downloadBytes));
                                    }
                                    break;
                                case DownloadManager.STATUS_FAILED:
                                    download = false;
                                    Log.d(TAG, "" + reason);
                                    //Mensaje de que la descarga falló
                                    runOnUiThread(() -> progressBar.setVisibility(ProgressBar.GONE));
                                    break;
                                case DownloadManager.STATUS_SUCCESSFUL:
                                    download = false;
                                    //Abrir el APK
                                    runOnUiThread(() -> progressBar.setVisibility(ProgressBar.GONE));
                                    break;
                            }
                        }
                    }
                }
            });

            thread.start();
        }else{
            Log.d(TAG, "Se acabaron los APK disponibles");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cvdownload);

        pruebaDeOpenCV();

        Button buttonInstalar = findViewById(R.id.opencv_instalar);
        Button buttonPermisos = findViewById(R.id.opencv_boton_permisos);
        progressBar = findViewById(R.id.opencv_progress);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED) {
            buttonPermisos.setOnClickListener(view -> {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, REQUEST_INSTALL_PACKAGE);
            });
        }else{
            botonPermisoInstalacion();
        }

        buttonInstalar.setOnClickListener(view -> {
            iniciarDescarga();
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), getCurrentFileName());

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    startActivity(install);
                }else {
                    Intent install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    Uri uriFile = FileProvider.getUriForFile(context, "com.megacode.fileprovider", file);
                    install.setData(uriFile);
                    install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(install);
                }
            }
        };

        intentFilterInstall = new IntentFilter();
        intentFilterInstall.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilterInstall.addDataScheme("package");

        broadcastReceiverInstallApp = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Fallo la carga de OpenCV, probar con otro instalador
                if (!pruebaDeOpenCV()){
                    descargaIndex++;

                    new AlertDialog.Builder(OpenCVDownload.this)
                            .setMessage("Parece que algio salio mal :( , OpenCV no funcionó correctamente por lo que se va a instalar otra versión")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    iniciarDescarga();
                                }
                            })
                            .create()
                            .show();
                }
            }
        };

    }

    @Override
    protected void onResume() {
        registerReceiver(broadcastReceiverInstallApp, intentFilterInstall);
        registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(broadcastReceiverInstallApp);
        unregisterReceiver(broadcastReceiver);

        super.onPause();
    }
}
