package com.megacode.viewmodels;

import android.app.Application;

import com.megacode.models.GeneralCallback;
import com.megacode.models.database.Usuario;
import com.megacode.repositories.UsuarioRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UsuarioViewModel extends AndroidViewModel {
    private UsuarioRepository usuarioRepository;

    public UsuarioViewModel(@NonNull Application application) {
        super(application);
        usuarioRepository = new UsuarioRepository(application);
    }

    public boolean hasUsuario(){
        return usuarioRepository.hasUsuario();
    }

    public LiveData<Usuario> obtenerUsuario(){
        return usuarioRepository.obtenerUsuario();
    }

    public void borrarUsuario(){
        usuarioRepository.borrarTodos();
    }

    public void insert(Usuario usuario){
        usuarioRepository.insert(usuario);
    }

    public void update(Usuario usuario){
        usuarioRepository.update(usuario);
    }
}
