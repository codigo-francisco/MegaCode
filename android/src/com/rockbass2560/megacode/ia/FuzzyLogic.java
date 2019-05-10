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
    public static boolean isEngineReady = false;

    public static class ConfigVariable{
        public double pocoTiempo, inicioRegularTiempo, finRegularTiempo, muchoTiempo;
        public double pocoAyudas, inicioRegularAyudas, finRegularAyudas, muchoAyudas;
        public double pocoErrores, inicioRegularErrores, finRegularErrores, muchoErrores;
    }

    public enum Dificultad{
        FACIL,
        REGULAR,
        DIFICIL;

        public static Dificultad parseValue(double value){
            Dificultad dificultad;

            if (value <= 3)
                dificultad = FACIL;
            else if (value <=6)
                dificultad = REGULAR;
            else
                dificultad = DIFICIL;

            return dificultad;
        }
    }

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

    public static FuzzyLogic configEngine(
            ConfigVariable configuracion
    ) throws Exception{

        if (fuzzyLogic == null){
            throw new Exception("La maquina de reglas difusa no ha sido inicializada");
        }

        FIS fis = fuzzyLogic.fis;

        //Configurando variables de tiempo
        Variable tiempo = fis.getVariable(Claves.VARIABLE_TIEMPO);

        tiempo.getLinguisticTerm(Claves.TERMINO_POCO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{Value.ZERO, new Value(configuracion.pocoTiempo)},
                        new Value[]{Value.ONE, Value.ONE}
                )
        );

        tiempo.getLinguisticTerm(Claves.TERMINO_REGULAR).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.inicioRegularTiempo), new Value(configuracion.finRegularTiempo)},
                        new Value[]{Value.ONE, Value.ONE}));

        tiempo.getLinguisticTerm(Claves.TERMINO_MUCHO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.finRegularTiempo), new Value(configuracion.muchoTiempo)},
                        new Value[]{Value.ZERO, Value.ONE}));


        //Configurando los terminos de ayudas
        Variable ayudas = fis.getVariable(Claves.VARIABLE_AYUDAS);

        ayudas.getLinguisticTerm(Claves.TERMINO_POCO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{Value.ZERO, new Value(configuracion.pocoAyudas)},
                        new Value[]{Value.ONE, Value.ONE}
                )
        );

        ayudas.getLinguisticTerm(Claves.TERMINO_REGULAR).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.inicioRegularAyudas), new Value(configuracion.finRegularAyudas)},
                        new Value[]{Value.ONE, Value.ONE}));

        ayudas.getLinguisticTerm(Claves.TERMINO_MUCHO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.finRegularAyudas), new Value(configuracion.muchoAyudas)},
                        new Value[]{Value.ZERO, Value.ONE}));

        //Configurando los terminos de errores
        Variable errores = fis.getVariable(Claves.VARIABLE_ERRORES);

        errores.getLinguisticTerm(Claves.TERMINO_POCO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{Value.ZERO, new Value(configuracion.pocoErrores)},
                        new Value[]{Value.ONE, Value.ONE}
                )
        );

        errores.getLinguisticTerm(Claves.TERMINO_REGULAR).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.inicioRegularErrores), new Value(configuracion.finRegularErrores)},
                        new Value[]{Value.ONE, Value.ONE}));

        errores.getLinguisticTerm(Claves.TERMINO_MUCHO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.finRegularErrores), new Value(configuracion.muchoErrores)},
                        new Value[]{Value.ZERO, Value.ONE}));

        isEngineReady = true;

        return fuzzyLogic;
    }

    public Dificultad getDificultad(double tiempo, double ayudas, double errores){
        fis.setVariable(Claves.VARIABLE_TIEMPO, tiempo);
        fis.setVariable(Claves.VARIABLE_AYUDAS, ayudas);
        fis.setVariable(Claves.VARIABLE_ERRORES, errores);

        fis.evaluate();

        //fis.getVariable(Claves.VARIABLE_DIFICULTAD).

        Variable dificultad = fis.getVariable(Claves.VARIABLE_DIFICULTAD);
        Dificultad dificultadResult = Dificultad.parseValue(dificultad.getValue());

        return dificultadResult;
    }
}