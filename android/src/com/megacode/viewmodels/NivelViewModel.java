package com.megacode.viewmodels;

import android.app.Application;

import com.megacode.models.database.Nivel;
import com.megacode.models.database.NivelConTerminado;
import com.megacode.repositories.NivelRepository;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class NivelViewModel extends AndroidViewModel {

    private NivelRepository nivelRepository;

    public NivelViewModel(@NonNull Application application) {
        super(application);
        nivelRepository = new NivelRepository(application);
    }

    public void listarNiveles(){
        nivelRepository.listarNiveles();
    }

    public MutableLiveData<LinkedList<List<NivelConTerminado>>> getNiveles() {
        return nivelRepository.getNiveles();
    }
}
