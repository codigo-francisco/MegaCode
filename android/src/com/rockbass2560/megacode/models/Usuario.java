package com.rockbass2560.megacode.models.database;

import com.google.firebase.firestore.Exclude;

/**
 * Clase que representa a la persona que realiza la sesión en el sistema
 */
public class Usuario {

    @Exclude
    public String id;
    @Exclude
    public String email;
    public String nombre;
    public String sexo;
    public int si;
    public int para;
    public int mientras;
    public int comandos;
    public String fotoPerfil;
    public int edad;

    public Usuario(){

    }


}
