package com.rockbass2560.megacode.models.database;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Sesion {
    @Exclude
    public String id;
    @Exclude
    public String usuarioId;
    @Exclude
    public List<Emocion> emociones = new ArrayList<>();
    public int nivelId;
    public String conexionId;
    public int tiempo;
    public int intentos;
    public int ayudas;
    public int inactividad;

}
