package com.megacode.helpers;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.megacode.screens.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

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

}
