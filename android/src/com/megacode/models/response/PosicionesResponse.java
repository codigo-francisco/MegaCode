package com.megacode.models.response;

public class PosicionesResponse {

    private String nombre;
    private short variabes;
    private short si;
    private short para;
    private short mientras;
    private short total;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public short getVariabes() {
        return variabes;
    }

    public void setVariabes(short variabes) {
        this.variabes = variabes;
    }

    public short getSi() {
        return si;
    }

    public void setSi(short si) {
        this.si = si;
    }

    public short getPara() {
        return para;
    }

    public void setPara(short para) {
        this.para = para;
    }

    public short getMientras() {
        return mientras;
    }

    public void setMientras(short mientras) {
        this.mientras = mientras;
    }

    public short getTotal() {
        return total;
    }

    public void setTotal(short total) {
        this.total = total;
    }
}
