package com.rockbass2560.megacode.models.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.rockbass2560.megacode.R;

public class Nivel implements Parcelable {
    public int id;
    public String nombre;
    public int dificultad;
    public int grupo;
    public int mientras;
    public int comandos;
    public int si;
    public int para;
    public String ruta;
    public int tipoNivel;
    public String cadenaOptima;
    public float zoomInicial;
    public int coordenadaX;
    public int coordenadaY;

    public Nivel() {

    }

    public String tipoNivelString(){
        String message = null;
        switch (tipoNivel){
            case 1:
                message = "Comando";
                break;
            case 2:
                message = "Si";
                break;
            case 3:
                message = "Para";
                break;
            case 4:
                message ="mientras";
                break;
            default:
                message="Sin Tipo";
        }

        return message;
    }

    public int getColorBackground(){
        int color;

        switch (tipoNivel){
            case 1:
                color = R.color.md_brown_600;
                break;
            case 2:
                color = R.color.md_blue_700;
                break;
            case 3:
                color = R.color.md_yellow_700;
                break;
            case 4:
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
        dest.writeString(this.cadenaOptima);
        dest.writeFloat(this.zoomInicial);
    }

    protected Nivel(Parcel in) {
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
        this.cadenaOptima = in.readString();
        this.zoomInicial = in.readFloat();
    }

    public static final Parcelable.Creator<Nivel> CREATOR = new Parcelable.Creator<Nivel>() {
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
