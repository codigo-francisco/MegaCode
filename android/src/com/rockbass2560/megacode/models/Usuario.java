package com.rockbass2560.megacode.models;

import com.google.firebase.firestore.Exclude;

/**
 * Clase que representa a la persona que realiza la sesión en el sistema
 */
public class Usuario {

    public String id;
    @Exclude
    public String email;
    public int si;
    public int para;
    public int mientras;
    public int comandos;
    public String fotoPerfil;

    public int getPuntajeTotal(){
        return comandos+si+para+mientras;
    }

    public Usuario(){

    }


}
