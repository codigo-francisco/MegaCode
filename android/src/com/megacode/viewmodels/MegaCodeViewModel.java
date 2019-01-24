package com.megacode.viewmodels;

import android.app.Application;

import com.megacode.adapters.model.DataModel;
import com.megacode.models.database.NivelTerminado;
import com.megacode.models.database.Usuario;
import com.megacode.repositories.FeedRepository;
import com.megacode.repositories.NivelRepository;
import com.megacode.repositories.NivelTerminadoRepository;
import com.megacode.repositories.UsuarioRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MegaCodeViewModel extends AndroidViewModel {

    private NivelTerminadoRepository nivelTerminadoRepository;
    private UsuarioRepository usuarioRepository;
    private FeedRepository feedRepository;
    private NivelRepository nivelRepository;

    public MegaCodeViewModel(@NonNull Application application) {
        super(application);

        nivelTerminadoRepository = new NivelTerminadoRepository(application);
        usuarioRepository = new UsuarioRepository(application);
        feedRepository = new FeedRepository(application);
        nivelRepository = new NivelRepository(application);
    }

    public void insertarNivelTerminadoSync(NivelTerminado nivelTerminado){
        nivelTerminadoRepository.insertarNivelTerminadoSync(nivelTerminado);
    }

    public LiveData<Usuario> getUsuario(){
        return usuarioRepository.obtenerUsuario();
    }

    public DataModel siguienteEjercicioSync(){
        return feedRepository.siguienteEjercicioSync();
    }

    public void refrescarNiveles(){
        nivelRepository.listarNiveles();
    }
}
