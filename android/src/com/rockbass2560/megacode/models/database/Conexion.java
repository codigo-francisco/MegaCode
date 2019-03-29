package com.rockbass2560.megacode.models.database;

import com.google.firebase.firestore.Exclude;

import java.util.Calendar;
import java.util.Date;

public class Conexion {
    @Exclude
    public String id;
    public String usuarioId;
    public long entrada;
    public long salida;
    public Integer duracion;
}
