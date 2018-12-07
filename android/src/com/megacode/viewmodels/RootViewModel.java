package com.megacode.viewmodels;

import android.app.Application;

import com.megacode.models.database.Usuario;
import com.megacode.repositories.UsuarioRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RootViewModel extends AndroidViewModel {
    private UsuarioRepository usuarioRepository;

    public RootViewModel(@NonNull Application application) {
        super(application);

        usuarioRepository = new UsuarioRepository(application);
    }

    public LiveData<Usuario> obtenerUsuario(){
        return usuarioRepository.obtenerUsuario();
    }
}
