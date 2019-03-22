package com.megacode.models.response;

import com.megacode.models.database.Usuario;

public class LoginResponse {
    private Usuario usuario;
    private String token;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
