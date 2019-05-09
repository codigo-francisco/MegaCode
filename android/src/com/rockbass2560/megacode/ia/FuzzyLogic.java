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
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionPieceWiseLinear;
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

            //Log.d(TAG,fuzzyLogic.fis.toStringFcl());

        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void testDefaultEngine(){

    }

    public FuzzyLogic configEngine(
            ConfigVariable configuracion
    ){

        FIS fis = fuzzyLogic.fis;

        //Configurando variables de tiempo
        Variable tiempo = fis.getVariable(Claves.VARIABLE_TIEMPO);

        tiempo.getLinguisticTerm(Claves.TERMINO_POCO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.tiempoPocoX)},
                        new Value[]{new Value(configuracion.tiempoPocoY)}));


        tiempo.getLinguisticTerm(Claves.TERMINO_REGULAR).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.tiempoRegularX)},
                        new Value[]{new Value(configuracion.tiempoRegularY)}));

        tiempo.getLinguisticTerm(Claves.TERMINO_MUCHO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.tiempoMuchoX)},
                        new Value[]{new Value(configuracion.tiempoMuchoY)}));


        //Configurando los terminos de ayudas
        Variable ayudas = fis.getVariable(Claves.VARIABLE_AYUDAS);

        ayudas.getLinguisticTerm(Claves.TERMINO_POCO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.ayudasPocoX)},
                        new Value[]{new Value(configuracion.ayudasPocoY)}));


        ayudas.getLinguisticTerm(Claves.TERMINO_REGULAR).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.ayudasRegularX)},
                        new Value[]{new Value(configuracion.ayudasRegularY)}));

        ayudas.getLinguisticTerm(Claves.TERMINO_MUCHO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.ayudasMuchoX)},
                        new Value[]{new Value(configuracion.ayudasMuchoY)}));

        //Configurando los terminos de errores
        Variable errores = fis.getVariable(Claves.VARIABLE_ERRORES);

        errores.getLinguisticTerm(Claves.TERMINO_POCO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.erroresPocoX)},
                        new Value[]{new Value(configuracion.erroresPocoY)}));


        errores.getLinguisticTerm(Claves.TERMINO_REGULAR).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.erroresRegularX)},
                        new Value[]{new Value(configuracion.erroresRegularY)}));

        errores.getLinguisticTerm(Claves.TERMINO_MUCHO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.erroresMuchoX)},
                        new Value[]{new Value(configuracion.erroresMuchoX)}));

        return fuzzyLogic;
    }

    public static class ConfigVariable{
        public double tiempoPocoX, tiempoPocoY, tiempoRegularX, tiempoRegularY, tiempoMuchoX, tiempoMuchoY;
        public double ayudasPocoX, ayudasPocoY, ayudasRegularX, ayudasRegularY, ayudasMuchoX, ayudasMuchoY;
        public double erroresPocoX, erroresPocoY, erroresRegularX, erroresRegularY, erroresMuchoX, erroresMuchoY;
    }
}