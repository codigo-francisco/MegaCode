package com.megacode.viewmodels;

import android.app.Application;

import com.megacode.adapters.model.DataModel;
import com.megacode.models.database.Usuario;
import com.megacode.repositories.FeedRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class FeedViewModel extends AndroidViewModel {

    private FeedRepository feedRepository;
    private MutableLiveData<List<DataModel>> dataModelMutableLiveData;

    public FeedViewModel(@NonNull Application application) {
        super(application);

        feedRepository = new FeedRepository(application);
        dataModelMutableLiveData = feedRepository.getDataModelMutableLiveData();
    }

    public MutableLiveData<List<DataModel>> actualizarFeed(boolean borrarFeed){
        return feedRepository.actualizarFeed(borrarFeed);
    }

    public MutableLiveData<List<DataModel>> getDataModelMutableLiveData() {
        return dataModelMutableLiveData;
    }
}
