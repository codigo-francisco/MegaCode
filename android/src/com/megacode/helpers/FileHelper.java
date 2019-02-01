package com.megacode.helpers;

import java.io.IOException;
import java.io.InputStream;

public class FileHelper {

    public static String getTextFile(InputStream file) throws IOException {
        int size = file.available();
        byte[] buffer = new byte[size];
        file.read(buffer);
        String text = new String(buffer);

        return text;
    }
}
