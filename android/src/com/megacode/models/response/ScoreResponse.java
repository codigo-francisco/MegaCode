package com.megacode.models.response;

import android.os.Parcel;
import android.os.Parcelable;

public class ScoreResponse implements Parcelable {

    private String nombre;
    private int score;
    private int dia;
    private int mes;
    private int anio;
    private String fotoPerfil;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getFotoPerfil(){
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil){
        this.fotoPerfil = fotoPerfil;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nombre);
        dest.writeInt(this.score);
        dest.writeInt(this.dia);
        dest.writeInt(this.mes);
        dest.writeInt(this.anio);
        dest.writeString(this.fotoPerfil);
    }

    public ScoreResponse() {
    }

    protected ScoreResponse(Parcel in) {
        this.nombre = in.readString();
        this.score = in.readInt();
        this.dia = in.readInt();
        this.mes = in.readInt();
        this.anio = in.readInt();
        this.fotoPerfil = in.readString();
    }

    public static final Parcelable.Creator<ScoreResponse> CREATOR = new Parcelable.Creator<ScoreResponse>() {
        @Override
        public ScoreResponse createFromParcel(Parcel source) {
            return new ScoreResponse(source);
        }

        @Override
        public ScoreResponse[] newArray(int size) {
            return new ScoreResponse[size];
        }
    };
}
