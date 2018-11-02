package com.megacode.viewmodels;

import android.app.Application;

import com.megacode.models.database.Usuario;
import com.megacode.repositories.LoginRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository loginRepository;
    private MutableLiveData<Usuario> usuarioMutableLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = new LoginRepository(application);
        usuarioMutableLiveData = loginRepository.getUsuarioLiveData();
    }

    public MutableLiveData<Usuario> loginUsuario(String email, String contrasena){
        return loginRepository.loginUsuario(email, contrasena);
    }

    public MutableLiveData<Usuario> registrarUsuario(Usuario usuario){
        return loginRepository.registrar(usuario);
    }

    public MutableLiveData<Usuario> getUsuarioMutableLiveData(){
        return usuarioMutableLiveData;
    }
}
