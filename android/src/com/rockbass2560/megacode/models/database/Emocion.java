package com.rockbass2560.megacode.models.database;

import com.google.firebase.Timestamp;

public class Emocion {
    public String label;
    public Timestamp momento;
    public int etapa; //1 bloque, 2 ejecucion, 3 finalizacion

    public int getNumber(){
        switch (label){
            case "bored":
                return 0;
            case "engaged":
                return 1;
            case "excited":
                return 2;
            case "focused":
                return 3;
            case "interested":
                return 4;
            case "neutral":
                return 5;
            default:
                return 0;
        }
    }
}
