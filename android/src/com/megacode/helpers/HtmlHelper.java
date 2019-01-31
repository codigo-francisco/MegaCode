package com.megacode.helpers;

import android.content.Context;
import android.util.Log;

import com.x5.template.Chunk;
import com.x5.template.Theme;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class HtmlHelper {

    private final static String TAG = HtmlHelper.class.getName();
    public static Theme theme;
    private final static String rutaBaseToolboxs = "blockly/toolboxs/";
    private final static String rutaBaseWorkspaces = "blockly/workspaces/";
    public final static String MIME = "text/html";
    public final static String ENCODING = "utf-8";

    @NotNull
    public static String generarHtml(@NotNull String rutaNivel, @NotNull Context context){
        String rutaPagina = rutaNivel.substring(rutaNivel.lastIndexOf("/")+1).replace(".dt",".xml");
        Chunk chunk = theme.makeChunk("template","chtml");
        try {
            //Traer texto del nivel
            try (InputStream stream = context.getAssets().open(rutaBaseToolboxs.concat(rutaPagina))) {
                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                String xml = new String(buffer);
                chunk.put("toolbox",xml);
            }

            //Traer texto del workspace
        }catch (IOException ex){
            Log.e(TAG, ex.getMessage(), ex);
        }

        return chunk.toString();
    }
}
