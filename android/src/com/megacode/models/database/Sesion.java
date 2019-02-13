package com.megacode.models.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Sesion",
    indices = {@Index("id"), @Index("nivelId"), @Index("usuarioId")},
    foreignKeys = {
        @ForeignKey(entity = Usuario.class, parentColumns = "id", childColumns = "usuarioId"),
        @ForeignKey(entity = Nivel.class, parentColumns = "id", childColumns = "nivelId"),
        @ForeignKey(entity = Sesion.class, parentColumns = "id", childColumns = "sesionId")
    }
)
public class Sesion {
    @PrimaryKey
    public long id;
    public long usuarioId;
    public int nivelId;
    public long conexionId;
    public int tiempo;
    public int intentos;
    public int ayudas;
    public int inactividad;
}
