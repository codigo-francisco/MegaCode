package com.megacode.models;

public class ScoreResponse {

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
}
