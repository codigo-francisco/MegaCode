package com.megacode.helpers;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImageProfileHelper {

    private final static String TAG = "ImageProfileHelper";

    public static String getDefaultProfileImage(AssetManager assetManager) {
        String result = "";
        try (InputStream inputStream = assetManager.open("perfilbase64.txt")){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            StringBuilder stringBuilder = new StringBuilder();
            while(inputStreamReader.ready()){
                stringBuilder.append((char)inputStreamReader.read());
            }

            result = stringBuilder.toString();
        }catch (IOException ex){
            Log.e(TAG, ex.getMessage(), ex);
        }

        return result;
    }

    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        Log.d(TAG, imageEncoded);
        return imageEncoded;
    }

}
