package com.megacode;

/**
 * Created by Francisco on 22/02/2018.
 */

public enum Comando {
    CAMINAR_DERECHA("derecha"),
    CAMINAR_IZQUIERDA("izquierda"),
    SALTAR("saltar"),
    DISPARAR("disparar"),
    NADA("nada");

    private String value;

    Comando(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}