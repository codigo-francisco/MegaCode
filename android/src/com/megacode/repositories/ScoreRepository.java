package com.megacode.repositories;

import android.util.Log;

import com.megacode.models.response.ScoreResponse;
import com.megacode.services.MegaCodeService;
import com.megacode.services.interfaces.ScoreService;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoreRepository {

    private static final String TAG = ScoreRepository.class.getName();
    private List<ScoreResponse> scoreModelList;
    private MutableLiveData<List<ScoreResponse>> scoreLiveData;

    public ScoreRepository(){
        scoreModelList = new ArrayList<>();
        scoreLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<ScoreResponse>> obtenerPuntajes() {

        scoreModelList.clear();

        MegaCodeService.getServicio(ScoreService.class).puntajes().enqueue(new Callback<List<ScoreResponse>>() {
            @Override
            public void onResponse(Call<List<ScoreResponse>> call, Response<List<ScoreResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size()>0) {
                        scoreModelList.addAll(response.body());

                        scoreLiveData.postValue(scoreModelList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ScoreResponse>> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
                scoreLiveData.postValue(null);
            }
        });

        return scoreLiveData;
    }

    public MutableLiveData<List<ScoreResponse>> getScoreLiveData() {
        return scoreLiveData;
    }
}
