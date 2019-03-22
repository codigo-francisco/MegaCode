package com.megacode.models;

public class FeedBack {

    private String contenido;
    private String titulo;

    public FeedBack(String titulo, String contenido){
        this.setTitulo(titulo);
        this.setContenido(contenido);
    }


    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
