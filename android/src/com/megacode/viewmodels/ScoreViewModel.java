package com.megacode.viewmodels;

import android.app.Application;

import com.megacode.models.response.ScoreResponse;
import com.megacode.repositories.ScoreRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ScoreViewModel extends AndroidViewModel {

    private ScoreRepository scoreRepository;

    public ScoreViewModel(@NonNull Application application) {
        super(application);
        scoreRepository = new ScoreRepository();
    }

    public MutableLiveData<List<ScoreResponse>> obtenerPuntajes(){
        return scoreRepository.obtenerPuntajes();
    }

    public MutableLiveData<List<ScoreResponse>> getListMutableLiveData() {
        return scoreRepository.getScoreLiveData();
    }

}
