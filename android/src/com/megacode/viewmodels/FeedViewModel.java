package com.megacode.viewmodels;

import android.app.Application;

import com.megacode.adapters.model.DataModel;
import com.megacode.repositories.FeedRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FeedViewModel extends AndroidViewModel {

    private FeedRepository feedRepository;
    private LiveData<List<DataModel>> feeds;

    public FeedViewModel(@NonNull Application application) {
        super(application);

        feedRepository = new FeedRepository(application);
        feeds = feedRepository.getFeeds();
    }

    public MutableLiveData<List<DataModel>> actualizarFeed(boolean borrarFeed){
        return feedRepository.actualizarFeed(borrarFeed);
    }

    public LiveData<List<DataModel>> getFeeds() {
        return feeds;
    }
}
