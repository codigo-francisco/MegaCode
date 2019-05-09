package com.rockbass2560.megacode.ia;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.rockbass2560.megacode.BuildConfig;
import com.rockbass2560.megacode.Claves;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionFuncion;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionGenericSingleton;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionSingleton;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionTriangular;
import net.sourceforge.jFuzzyLogic.membership.Value;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;


public class FuzzyLogic {
    private final static String TAG = FuzzyLogic.class.getName();

    private static FuzzyLogic fuzzyLogic;
    private FIS fis;

    private FuzzyLogic(){

    }

    public static void init(Context context){
        try {
            fuzzyLogic = new FuzzyLogic();

            InputStream fuzzyFileStream = context.getAssets().open("FuzzyRules.fcl");
            boolean isDebuggable =  ( 0 != ( context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
            fuzzyLogic.fis = FIS.load(fuzzyFileStream, isDebuggable);

        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void testDefaultEngine(){
        ConfigVariable tiempoConfig = new ConfigVariable();
        tiempoConfig.firstRange = 0;
        tiempoConfig.secondRange = 20;
        tiempoConfig.valorMasAlto = Double.MAX_VALUE;

        ConfigVariable erroresConfig = new ConfigVariable();
        erroresConfig.firstRange = 0;
        erroresConfig.secondRange = 10;
        erroresConfig.valorMasAlto = 10;

        ConfigVariable ayudasConfig = new ConfigVariable();
        ayudasConfig.firstRange = 0;
        ayudasConfig.secondRange = 20;
        ayudasConfig.valorMasAlto = 20;
    }

    public static FuzzyLogic configEngine(
            ConfigVariable tiempoConfig,
            ConfigVariable ayudaConfig,
            ConfigVariable erroresConfig
    ){

        return fuzzyLogic;
    }

    public static class ConfigVariable{
        public double firstRange, secondRange;
        public double valorMasAlto;
    }
}