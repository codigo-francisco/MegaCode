package com.udacity.gamedev.gigagal;

/**
 * Created by Francisco on 22/02/2018.
 */

enum Comando {
    CAMINAR_DERECHA(0),
    CAMINAR_IZQUIERDA(1),
    SALTAR(2),
    DISPARAR(3);

    private int value;

    private Comando(int value){
        this.value = value;
    }
}