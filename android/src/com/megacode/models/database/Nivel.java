package com.megacode.models.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.megacode.R;
import com.megacode.adapters.model.enumators.TypeLevel;
import com.megacode.models.database.converters.DateConverter;
import com.megacode.models.database.converters.TypeLevelConverter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@TypeConverters({TypeLevelConverter.class, DateConverter.class})
@Entity(tableName = "Nivel")
public class Nivel implements Parcelable {
    @Ignore
    private TypeLevel typeLevel;
    private Date lastRefresh;
    @PrimaryKey
    private int id;
    private String nombre;
    private int dificultad;
    private int grupo;
    private int mientras;
    private int comandos;
    private int si;
    private int para;
    private String ruta;
    private int tipoNivel;

    public Nivel() {
        lastRefresh = new Date();
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public int getPara() {
        return para;
    }

    public void setPara(int para) {
        this.para = para;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public TypeLevel getTypeLevel() {
        return typeLevel;
    }

    public void setTypeLevel(TypeLevel typeLevel){
        this.typeLevel = typeLevel;
    }

    public Date getLastRefresh(){
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public int getComando() {
        return comandos;
    }

    public void setComando(int comando) {
        this.comandos = comando;
    }

    public int getSi() {
        return si;
    }

    public void setSi(int si) {
        this.si = si;
    }

    public int getMientras() {
        return mientras;
    }

    public void setMientras(int mientras) {
        this.mientras = mientras;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComandos() {
        return comandos;
    }

    public void setComandos(int comandos) {
        this.comandos = comandos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public int getTipoNivel() {
        return tipoNivel;
    }

    public void setTipoNivel(int tipoNivel) {
        this.tipoNivel = tipoNivel;
        chooseTypeLevel();
    }

    public void chooseTypeLevel(){
        this.typeLevel = TypeLevel.values()[tipoNivel - 1];
    }

    public int getColorBackground(){
        int color;

        switch (getTypeLevel()){
            case COMANDO:
                color = R.color.md_brown_600;
                break;
            case SI:
                color = R.color.md_blue_700;
                break;
            case PARA:
                color = R.color.md_yellow_700;
                break;
            case MIENTRAS:
                color = R.color.md_purple_600;
                break;
            default:
                color = R.color.md_blue_700;
                break;
        }

        return color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.typeLevel == null ? -1 : this.typeLevel.ordinal());
        dest.writeLong(this.lastRefresh != null ? this.lastRefresh.getTime() : -1);
        dest.writeInt(this.id);
        dest.writeString(this.nombre);
        dest.writeInt(this.dificultad);
        dest.writeInt(this.grupo);
        dest.writeInt(this.mientras);
        dest.writeInt(this.comandos);
        dest.writeInt(this.si);
        dest.writeInt(this.para);
        dest.writeString(this.ruta);
        dest.writeInt(this.tipoNivel);
    }

    protected Nivel(Parcel in) {
        int tmpTypeLevel = in.readInt();
        this.typeLevel = tmpTypeLevel == -1 ? null : TypeLevel.values()[tmpTypeLevel];
        long tmpLastRefresh = in.readLong();
        this.lastRefresh = tmpLastRefresh == -1 ? null : new Date(tmpLastRefresh);
        this.id = in.readInt();
        this.nombre = in.readString();
        this.dificultad = in.readInt();
        this.grupo = in.readInt();
        this.mientras = in.readInt();
        this.comandos = in.readInt();
        this.si = in.readInt();
        this.para = in.readInt();
        this.ruta = in.readString();
        this.tipoNivel = in.readInt();
    }

    public static final Creator<Nivel> CREATOR = new Creator<Nivel>() {
        @Override
        public Nivel createFromParcel(Parcel source) {
            return new Nivel(source);
        }

        @Override
        public Nivel[] newArray(int size) {
            return new Nivel[size];
        }
    };
}
