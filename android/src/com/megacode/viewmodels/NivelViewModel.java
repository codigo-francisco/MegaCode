package com.megacode.viewmodels;

import android.app.Application;

import com.megacode.adapters.model.DataModel;
import com.megacode.models.database.Nivel;
import com.megacode.models.database.NivelConTerminado;
import com.megacode.repositories.FeedRepository;
import com.megacode.repositories.NivelRepository;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NivelViewModel extends AndroidViewModel {

    private NivelRepository nivelRepository;
    private FeedRepository feedRepository;

    public NivelViewModel(@NonNull Application application) {
        super(application);
        nivelRepository = new NivelRepository(application);
        feedRepository = new FeedRepository(application);
    }

    public void listarNiveles(){
        nivelRepository.listarNiveles();
    }

    public MutableLiveData<LinkedList<List<NivelConTerminado>>> getNiveles() {
        return nivelRepository.getNiveles();
    }

    public LiveData<DataModel> getSiguienteEjercicio(){
        return feedRepository.siguienteEjercicio();
    }
}
