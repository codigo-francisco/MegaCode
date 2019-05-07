package com.rockbass2560.megacode.ia;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.fuzzylite.Engine;
import com.fuzzylite.Op;
import com.fuzzylite.activation.General;
import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.imex.FldExporter;
import com.fuzzylite.imex.FllImporter;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.AlgebraicProduct;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Ramp;
import com.fuzzylite.term.Trapezoid;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;
import com.google.common.io.Files;
import com.rockbass2560.megacode.Claves;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

/**
 *
 */
public class FuzzyLogic {
    private final static String TAG = FuzzyLogic.class.getName();

    private static FuzzyLogic fuzzyLogic;

    /**
     *
     */
    private FuzzyLogic(){

    }

    private Engine engine;

    /**
     * Inicializa el motor FuzzyLogic
     */
    public static void init(){
        //TODO: Hay un bug que no permite la importanción de archivos
        fuzzyLogic = new FuzzyLogic();
        Engine engine = new Engine();
        fuzzyLogic.engine = engine;

        engine.setName("decision_pedagogica");
        engine.setDescription(Claves.EMPTY_STRING);
    }

    public static FuzzyLogic configEngine(
            ConfigVariable tiempoConfig,
            ConfigVariable ayudaConfig,
            ConfigVariable erroresConfig
    ){
        Engine engine = fuzzyLogic.engine;

        InputVariable tiempo = new InputVariable();
        tiempo.setName("tiempo");
        tiempo.setDescription(Claves.EMPTY_STRING);
        tiempo.setEnabled(true);
        tiempo.setRange(tiempoConfig.firstRange, tiempoConfig.secondRange);
        tiempo.setLockValueInRange(false);
        tiempo.addTerm(new Ramp("bajo", 0, tiempoConfig.tiempoAlto));
        tiempo.addTerm(new Ramp("alto", tiempoConfig.ayudaAlta, 0));
        engine.addInputVariable(tiempo);

        InputVariable ayuda = new InputVariable();
        ayuda.setName("ayuda");
        ayuda.setDescription(Claves.EMPTY_STRING);
        ayuda.setEnabled(true);
        ayuda.setRange(ayudaConfig.firstRange, ayudaConfig.secondRange);
        ayuda.setLockValueInRange(false);
        ayuda.addTerm(new Ramp("bajo", 0, ayudaConfig.ayudaAlta));
        ayuda.addTerm(new Ramp("alto", ayudaConfig.ayudaAlta, 0));
        engine.addInputVariable(ayuda);

        InputVariable errores = new InputVariable();
        errores.setName("errores");
        errores.setDescription(Claves.EMPTY_STRING);
        errores.setEnabled(true);
        errores.setRange(erroresConfig.firstRange, erroresConfig.secondRange);
        errores.setLockValueInRange(false);
        errores.addTerm(new Ramp("bajo", 0, erroresConfig.erroresAlto));
        errores.addTerm(new Ramp("alto", erroresConfig.erroresAlto, 0));
        engine.addInputVariable(errores);

        OutputVariable dificultad = new OutputVariable();
        dificultad.setName("dificultad");
        dificultad.setDescription(Claves.EMPTY_STRING);
        dificultad.setEnabled(true);
        dificultad.setRange(0,1);
        dificultad.setLockValueInRange(false);
        dificultad.setAggregation(new Maximum());
        dificultad.setDefuzzifier(new Centroid(100));
        dificultad.setDefaultValue(Double.NaN);
        dificultad.setLockPreviousValue(false);
        dificultad.addTerm(new Trapezoid("facil", 0, 0,.25,.5));
        dificultad.addTerm(new Trapezoid("normal", .25, .35,.65,.7));
        engine.addOutputVariable(dificultad);

        RuleBlock ruleBlock = new RuleBlock();
        ruleBlock.setName("RuleBlock");
        ruleBlock.setDescription(Claves.EMPTY_STRING);
        ruleBlock.setEnabled(true);
        ruleBlock.setConjunction(null);
        ruleBlock.setDisjunction(null);
        ruleBlock.setImplication(new AlgebraicProduct());
        ruleBlock.setActivation(new General());
        ruleBlock.addRule(Rule.parse("if tiempo is bajo and errores is bajo and ayudas is bajo then dificultad is facil", engine));
        ruleBlock.addRule(Rule.parse("if tiempo is bajo and errores is bajo and ayudas is alto then dificultad is facil", engine));
        ruleBlock.addRule(Rule.parse("if tiempo is bajo and errores is alto and ayudas is bajo then dificultad is facil", engine));
        ruleBlock.addRule(Rule.parse("if tiempo is bajo and errores is alto and ayudas is alto then dificultad is facil", engine));
        ruleBlock.addRule(Rule.parse("if tiempo is alto and errores is bajo and ayudas is bajo then dificultad is facil", engine));
        ruleBlock.addRule(Rule.parse("if tiempo is alto and errores is bajo and ayudas is alto then dificultad is facil", engine));
        ruleBlock.addRule(Rule.parse("if tiempo is alto and errores is alto and ayudas is bajo then dificultad is facil", engine));
        ruleBlock.addRule(Rule.parse("if tiempo is alto and errores is alto and ayudas is alto then dificultad is facil", engine));
        engine.addRuleBlock(ruleBlock);

        return fuzzyLogic;
    }

    /**
     *
     */
    public static class ConfigVariable{
        public double firstRange, secondRange;
        public double tiempoAlto, ayudaAlta, erroresAlto;
    }
}