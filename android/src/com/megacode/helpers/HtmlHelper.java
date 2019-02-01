package com.megacode.helpers;

import android.content.Context;
import android.util.Log;

import com.x5.template.Chunk;
import com.x5.template.Theme;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class HtmlHelper {

    private final static String TAG = HtmlHelper.class.getName();
    public static Theme theme;
    private final static String rutaBaseToolboxs = "blockly/toolboxs/";
    private final static String rutaBaseWorkspaces = "blockly/workspaces/";
    private final static String workspacesPath = "blockly/workspaces";
    public final static String MIME = "text/html";
    public final static String ENCODING = "utf-8";

    @NotNull
    public static String generarHtml(@NotNull String rutaNivel, @NotNull Context context){
        String rutaPagina = rutaNivel.substring(rutaNivel.lastIndexOf("/")+1).replace(".dt",".xml");
        String rutaWorkspace = rutaNivel.substring(rutaNivel.lastIndexOf("/")+1).replace(".dt",".html");

        Chunk chunk = theme.makeChunk("template","chtml");
        try {
            //Traer texto del nivel
            try (InputStream stream = context.getAssets().open(rutaBaseToolboxs.concat(rutaPagina))) {
                String xml = FileHelper.getTextFile(stream);
                chunk.put("toolbox", xml);
            }

            //Traer texto del workspace
            String rutaWorkspaceArchivo = rutaBaseWorkspaces.concat(rutaWorkspace);
            if (Arrays.asList(context.getAssets().list(workspacesPath)).contains(rutaWorkspace)){
                try(InputStream streamWorkspace = context.getAssets().open(rutaWorkspaceArchivo)) {
                    String xmlWorkspace = FileHelper.getTextFile(streamWorkspace);
                    chunk.put("workspace", xmlWorkspace);
                }
            }
        }catch (IOException ex){
            Log.e(TAG, ex.getMessage(), ex);
        }

        return chunk.toString();
    }
}
