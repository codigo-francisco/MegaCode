package com.megacode.screens;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.Locale;

public class OpenCVDownload extends AppCompatActivity {

    private final static String TAG = "OpenCVDownload";

    private long downloadReference;
    private ProgressBar progressBar;
    private DownloadManager downloadManager;
    private boolean changeProgress;
    private static int REQUEST_INSTALL_PACKAGE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==REQUEST_INSTALL_PACKAGE){
            if (grantResults.length<1){
                botonPermisoInstalacion();
            }else{
                //Permiso rechazado
            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cvdownload);

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
            String abi = Build.SUPPORTED_ABIS[0];

            downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
            Uri uri = Uri.parse("http://192.168.1.83/megacode/api/archivos/"+abi);
            DownloadManager.Request request = new DownloadManager.Request(uri);

            request.setTitle("Descargando OpenCV Manager");
            request.setDescription("Se está descargando OpenCV Manager, al finalizar le solicitara que instale el APK");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            String fileName = String.format(Locale.getDefault(),"OpenCV_Manager_%s.apk",abi);
            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, fileName);

            downloadReference = downloadManager.enqueue(request);

            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Intent install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
                    Uri uriFile = FileProvider.getUriForFile(context, "com.megacode.fileprovider", file);
                    install.setData(uriFile);
                    install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(install);
                }
            };

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //Paquete instalado


                    unregisterReceiver(this);
                }
            },new IntentFilter(Intent.ACTION_PACKAGE_ADDED));

            registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            progressBar.setVisibility(ProgressBar.VISIBLE);

            Thread thread = new Thread(() -> {
                boolean download = true;
                changeProgress = false;

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadReference);

                while (download){
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int status = cursor.getInt(statusIndex);

                        int reasonIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                        int reason = cursor.getInt(reasonIndex);

                        int totalBytesIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                        int totalBytes = cursor.getInt(totalBytesIndex);

                        int downloadBytesIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                        int downloadBytes = cursor.getInt(downloadBytesIndex);

                        switch (status){
                            case DownloadManager.STATUS_RUNNING:
                                if (totalBytes>-1 && !changeProgress){
                                    runOnUiThread(()-> {
                                        changeProgress = true;
                                        progressBar.setMax(totalBytes);
                                        progressBar.setIndeterminate(false);
                                    });
                                }else if (changeProgress){
                                    runOnUiThread(()->progressBar.setProgress(downloadBytes));
                                }
                                break;
                            case DownloadManager.STATUS_FAILED:
                                download=false;
                                Log.d(TAG, ""+reason);
                                //Mensaje de que la descarga falló
                                runOnUiThread(() -> progressBar.setVisibility(ProgressBar.GONE));
                                break;
                            case DownloadManager.STATUS_SUCCESSFUL:
                                download=false;
                                //Abrir el APK
                                runOnUiThread(() -> progressBar.setVisibility(ProgressBar.GONE));
                                break;
                        }
                    }
                }
            });

            thread.start();
        });
    }
}
