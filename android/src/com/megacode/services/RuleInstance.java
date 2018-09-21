package com.megacode.services;

import com.megacode.models.FeedBack;
import com.megacode.models.Persona;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;

import java.util.ArrayList;
import java.util.List;

public class RuleInstance {
    private static RuleInstance ruleInstance;
    private RulesEngine rulesEngine;

    private Persona persona;
    private List<FeedBack> feedBacks;
    private Rules rules;
    private Facts facts;

    public static RuleInstance getRuleInstance(Persona persona){
        if (ruleInstance == null) {
            ruleInstance = new RuleInstance();
            ruleInstance.rules = new Rules();
            ruleInstance.rulesEngine = new DefaultRulesEngine();
            ruleInstance.feedBacks = new ArrayList<>();
            ruleInstance.facts = new Facts();

            ruleInstance.createRules();
        }

        //Se establecen las condiciones de la persona como hechos
        ruleInstance.setPersona(persona);

        return ruleInstance;
    }

    private void createRules(){
        Rule noExcercise = new RuleBuilder()
                .name("Sin ejercicios")
                .when(facts ->{
                        Persona persona = facts.get("persona");
                        return persona.getVariables() + persona.getSi() + persona.getPara() + persona.getMientras() < 1 ;
                })
                .then(facts -> feedBacks.add(new FeedBack("Consejo", "Te recomiendo probar un ejericio, comienza a jugar")))
                .build();
        rules.register(noExcercise);

        Rule siCompleto = new RuleBuilder()
                .name("Si completo")
                .when(facts -> ((Persona)facts.get("persona")).getSi()==5)
                .then(facts -> feedBacks.add(new FeedBack("Consejo","Terminaste los ejercicios con si, prueba ejercicios con otro tipo de temas")))
                .build();
        rules.register(siCompleto);
    }

    public void setPersona(Persona persona){
        this.persona = persona;
        this.facts.put("persona", persona);
    }

    public List<FeedBack> getFeedbacks(){
        feedBacks.clear();
        //Fire rules
        rulesEngine.fire(rules, facts);

        return feedBacks;
    }

    //Singleton
    private RuleInstance(){
    }
}
