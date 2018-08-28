package com.megacode.models.response;

import com.megacode.models.Persona;

public class LoginResponse {
    private Persona usuario;
    private String token;

    public Persona getUsuario() {
        return usuario;
    }

    public void setUsuario(Persona usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
