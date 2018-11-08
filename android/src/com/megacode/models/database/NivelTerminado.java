package com.megacode.models.database;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "NivelTerminado",
    indices = { @Index("usuarioId"),@Index("nivelId")},
    foreignKeys = {
            @ForeignKey(
                    entity = Usuario.class,
                    parentColumns = "id",
                    childColumns = "usuarioId"
            ),
            @ForeignKey(
                    entity = Nivel.class,
                    parentColumns = "id",
                    childColumns = "nivelId"
            )
    })
public class NivelTerminado {
    @PrimaryKey
    private long id;
    private int nivelId;
    private long usuarioId;
    private boolean terminado;
    private int puntaje;

    public NivelTerminado(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNivelId() {
        return nivelId;
    }

    public void setNivelId(int nivelId) {
        this.nivelId = nivelId;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public boolean isTerminado() {
        return terminado;
    }

    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
}
