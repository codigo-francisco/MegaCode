package com.udacity.gamedev.gigagal.android;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.blockly.android.AbstractBlocklyFragment;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.model.DefaultBlocks;
import com.udacity.gamedev.gigagal.GameplayScreen;
import com.udacity.gamedev.gigagal.GigaGalGame;
import com.udacity.gamedev.gigagal.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Francisco on 22/02/2018.
 */

public class BlocklyFragment extends AbstractBlocklyFragment {

    public final static String TAG = "BlocklyFragment";

    @NonNull
    @Override
    protected String getToolboxContentsXmlPath() {
        return "toolbox.xml";
    }

    @NonNull
    @Override
    protected List<String> getBlockDefinitionsJsonPaths() {
        List<String> assetPaths = new ArrayList<>(DefaultBlocks.getAllBlockDefinitions());
        // Append your own block definitions here.
        assetPaths.add("blocks.json");
        return assetPaths;
    }

    private static final List<String> JAVASCRIPT_GENERATORS = Arrays.asList(
            // Custom block generators go here. Default blocks are already included.
            "generators.js"
    );

    @NonNull
    @Override
    protected List<String> getGeneratorsJsPaths() {
        return JAVASCRIPT_GENERATORS;
    }

    CodeGenerationRequest.CodeGeneratorCallback mCodeGeneratorCallback = null;

    private static Level level;

    @NonNull
    @Override
    protected CodeGenerationRequest.CodeGeneratorCallback getCodeGenerationCallback() {
        if (mCodeGeneratorCallback==null) {
            mCodeGeneratorCallback = new CodeGenerationRequest.CodeGeneratorCallback() {

                @Override
                public void onFinishCodeGeneration(String generatedCode) {

                    if (level == null) {
                        level = ((GameplayScreen)GameFragment.GAME.getScreen()).level;
                    }

                    Log.d(TAG, generatedCode);

                    String[] comandos = generatedCode.split(",");

                    Log.d(TAG, "Procesando comandos: " + comandos);

                    level.setComandos(new LinkedList<String>(Arrays.asList(comandos)));
                    level.procesarComandos();
                }
            };
        }
        return mCodeGeneratorCallback;
    }
}
