package com.rockbass2560.megacode.services;

import com.rockbass2560.megacode.models.FeedBack;
import com.rockbass2560.megacode.models.Usuario;

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

    private Usuario usuario;
    private List<FeedBack> feedBacks;
    private Rules rules;
    private Facts facts;

    public static RuleInstance getRuleInstance(Usuario usuario){
        if (ruleInstance == null) {
            ruleInstance = new RuleInstance();
            ruleInstance.rules = new Rules();
            ruleInstance.rulesEngine = new DefaultRulesEngine();
            ruleInstance.feedBacks = new ArrayList<>();
            ruleInstance.facts = new Facts();

            ruleInstance.createRules();
        }

        //Se establecen las condiciones de la usuario como hechos
        ruleInstance.setUsuario(usuario);

        return ruleInstance;
    }

    private void createRules(){
        Rule noExcercise = new RuleBuilder()
                .name("Sin ejercicios")
                .when(facts ->{
                        Usuario usuario = facts.get("usuario");
                        return usuario.getPuntajeTotal() < 1 ;
                })
                .then(facts -> feedBacks.add(new FeedBack("Consejo", "Te recomiendo probar un ejericio, comienza a jugar")))
                .build();
        rules.register(noExcercise);

        Rule siCompleto = new RuleBuilder()
                .name("Si completo")
                .when(facts -> ((Usuario)facts.get("usuario")).si > 3)
                .then(facts -> feedBacks.add(new FeedBack("Consejo","Parece que ya probaste los ejercicios si, prueba ejercicios con otro tipo de temas")))
                .build();
        rules.register(siCompleto);

        Rule paraCompleto = new RuleBuilder()
                .name("Para Completo")
                .when(facts ->
                    ((Usuario)facts.get("usuario")).para > 3
                ).then(facts -> {
                    feedBacks.add(new FeedBack(
                            "Consejo",
                            "Parece que ya probaste los ejercicios con Para, estás avanzando"
                    ));
                })
                .build();
        rules.register(paraCompleto);

        Rule buenAvance = new RuleBuilder()
                .name("Buen Avance")
                .when(facts ->((Usuario)facts.get("usuario")).getPuntajeTotal() > 19)
                .then(facts -> {
                    feedBacks.add(new FeedBack(
                            "Felicitación",
                            "Has sumado más de 20 puntos, notable avance. Felicidades"
                    ));
                })
                .build();
        rules.register(buenAvance);
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
        this.facts.put("usuario", usuario);
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
