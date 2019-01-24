package com.megacode.models.database;

import android.os.Parcel;
import android.os.Parcelable;

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
public class NivelTerminado implements Parcelable {
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

    public long getNewId(){
        return Long.parseLong(Long.toString(nivelId) + usuarioId);
    }

    public void setNewId(){
        id = Long.parseLong(Long.toString(nivelId) + usuarioId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.nivelId);
        dest.writeLong(this.usuarioId);
        dest.writeByte(this.terminado ? (byte) 1 : (byte) 0);
        dest.writeInt(this.puntaje);
    }

    protected NivelTerminado(Parcel in) {
        this.id = in.readLong();
        this.nivelId = in.readInt();
        this.usuarioId = in.readLong();
        this.terminado = in.readByte() != 0;
        this.puntaje = in.readInt();
    }

    public static final Parcelable.Creator<NivelTerminado> CREATOR = new Parcelable.Creator<NivelTerminado>() {
        @Override
        public NivelTerminado createFromParcel(Parcel source) {
            return new NivelTerminado(source);
        }

        @Override
        public NivelTerminado[] newArray(int size) {
            return new NivelTerminado[size];
        }
    };
}
