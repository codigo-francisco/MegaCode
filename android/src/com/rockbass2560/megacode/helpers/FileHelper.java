package com.megacode.helpers;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileHelper {

    private final static String TAG = FileHelper.class.getName();

    public static String getTextFile(InputStream file) throws IOException {
        int size = file.available();
        byte[] buffer = new byte[size];
        file.read(buffer);
        String text = new String(buffer);

        return text;
    }

    public static ByteBuffer assetToByteBuffer(String assetName, Context context){
        AssetManager assetManager = context.getAssets();
        ByteBuffer byteBuffer = null;

        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(assetName);
            FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
            FileChannel fileChannel = fileInputStream.getChannel();
            long startOffset = assetFileDescriptor.getStartOffset();
            long declaredLength = assetFileDescriptor.getDeclaredLength();

            byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return byteBuffer;
    }
}
