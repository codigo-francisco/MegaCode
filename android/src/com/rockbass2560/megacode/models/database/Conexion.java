package com.rockbass2560.megacode.models.database;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

import java.util.Calendar;
import java.util.Date;

public class Conexion {
    @Exclude
    public String id;
    public DocumentReference usuarioId;
    public Timestamp entrada;
    public Timestamp salida;
    public Long duracion;
    public Long conexiones;
    public Long desconexiones;
}
