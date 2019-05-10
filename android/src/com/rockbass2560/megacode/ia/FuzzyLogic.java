package com.rockbass2560.megacode.ia;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.rockbass2560.megacode.Claves;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionPieceWiseLinear;
import net.sourceforge.jFuzzyLogic.membership.Value;
import net.sourceforge.jFuzzyLogic.rule.Rule;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class FuzzyLogic {
    private final static String TAG = FuzzyLogic.class.getName();

    private static FuzzyLogic fuzzyLogic;
    private FIS fis;
    public static boolean isEngineReady = false;

    public static class ConfigVariable{
        public double pocoTiempo, regularTiempo, muchoTiempo;
        public double pocoAyudas, regularAyudas, muchoAyudas;
        public double pocoErrores, regularErrores, muchoErrores;
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

            Log.d(TAG,fuzzyLogic.fis.getFunctionBlock(Claves.FUNCTION_BLOCK).getFuzzyRuleBlock("No1").toStringFcl());

        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void testFuzzyLogicExample(Context context){
        try {
            InputStream fileStream = context.getAssets().open("FuzzyRulesTest.fcl");
            FIS fis = FIS.load(fileStream, true);
            fis.setVariable("service", 3); // Set inputs
            fis.setVariable("food", 7);
            fis.evaluate(); // Evaluate

            // Show output variable
            Log.d(TAG,"Output value:" + fis.getVariable("tip").getValue());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    public static void testDefaultEngine(){
        FIS fis = fuzzyLogic.fis;

        Random random = new Random();

        for (int index=0; index < 10; index++){
            int tiempo = random.nextInt(10);
            int ayudas = random.nextInt(10);
            int errores = random.nextInt(10);

            fis.setVariable(Claves.VARIABLE_EMOCION, 0);
            fis.setVariable(Claves.VARIABLE_TIEMPO, tiempo);
            fis.setVariable(Claves.VARIABLE_AYUDAS, ayudas);
            fis.setVariable(Claves.VARIABLE_ERRORES, errores);

            fis.evaluate();

            /*for (Rule rule : fis.getFunctionBlock(Claves.FUNCTION_BLOCK).getFuzzyRuleBlock("No1").getRules()){
                Log.d(TAG, rule.toString());
                System.out.println(rule);
            }*/
        }
    }

    public static FuzzyLogic configEngine(ConfigVariable configuracion) throws Exception{

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
                        new Value[]{new Value(configuracion.pocoTiempo), new Value(configuracion.regularTiempo)},
                        new Value[]{Value.ONE, Value.ONE}));

        tiempo.getLinguisticTerm(Claves.TERMINO_MUCHO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.regularTiempo), new Value(configuracion.muchoTiempo)},
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
                        new Value[]{new Value(configuracion.pocoAyudas), new Value(configuracion.regularAyudas)},
                        new Value[]{Value.ONE, Value.ONE}));

        ayudas.getLinguisticTerm(Claves.TERMINO_MUCHO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.regularAyudas), new Value(configuracion.muchoAyudas)},
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
                        new Value[]{new Value(configuracion.pocoErrores), new Value(configuracion.regularErrores)},
                        new Value[]{Value.ONE, Value.ONE}));

        errores.getLinguisticTerm(Claves.TERMINO_MUCHO).setMembershipFunction(
                new MembershipFunctionPieceWiseLinear(
                        new Value[]{new Value(configuracion.regularErrores), new Value(configuracion.muchoErrores)},
                        new Value[]{Value.ZERO, Value.ONE}));

        isEngineReady = true;

        return fuzzyLogic;
    }

    public Dificultad getDificultad(int emocion, double tiempo, double ayudas, double errores){
        fis.setVariable(Claves.VARIABLE_EMOCION, emocion);
        fis.setVariable(Claves.VARIABLE_TIEMPO, tiempo);
        fis.setVariable(Claves.VARIABLE_AYUDAS, ayudas);
        fis.setVariable(Claves.VARIABLE_ERRORES, errores);

        fis.evaluate();

        Variable dificultad = fis.getVariable(Claves.VARIABLE_DIFICULTAD);
        Dificultad dificultadResult = Dificultad.parseValue(dificultad.getValue());

        return dificultadResult;
    }
}