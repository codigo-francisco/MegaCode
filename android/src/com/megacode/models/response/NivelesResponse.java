package com.megacode.models.response;

public class NivelesResponse {

    private int id;
    private String nombre;
    private String ruta;
    private int variables;
    private int si;
    private int para;
    private int mientras;
    private int dificultad;
    private int grupo;
    private int tipoNivel;

    public int getTipoNivel() {
        return tipoNivel;
    }

    public void setTipoNivel(int tipoNivel) {
        this.tipoNivel = tipoNivel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getVariables() {
        return variables;
    }

    public void setVariables(int variables) {
        this.variables = variables;
    }

    public int getSi() {
        return si;
    }

    public void setSi(int si) {
        this.si = si;
    }

    public int getPara() {
        return para;
    }

    public void setPara(int para) {
        this.para = para;
    }

    public int getMientras() {
        return mientras;
    }

    public void setMientras(int mientras) {
        this.mientras = mientras;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }
}
