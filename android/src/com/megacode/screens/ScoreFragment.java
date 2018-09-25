package com.megacode.screens;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.megacode.models.ScoreResponse;
import com.megacode.services.MegaCodeService;
import com.megacode.services.MegaCodeServiceInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {

    private final static String TAG = "ScoreFragment";
    private ScoreAdapter scoreAdapter;
    private List<ScoreResponse> scoreModelList;

    public ScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.scores_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        scoreModelList = new ArrayList<>();
        scoreAdapter = new ScoreAdapter(scoreModelList);

        recyclerView.setAdapter(scoreAdapter);

        createScoreModel();

        return view;
    }

    private void createScoreModel(){
        MegaCodeServiceInstance.getMegaCodeServiceInstance().megaCodeService.puntajes().enqueue(new Callback<List<ScoreResponse>>() {
            @Override
            public void onResponse(Call<List<ScoreResponse>> call, Response<List<ScoreResponse>> response) {
                if (response.isSuccessful()){
                    scoreModelList.addAll(response.body());

                    scoreAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ScoreResponse>> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
                Toast.makeText(getContext(), "No se han podido cargar los puntajes", Toast.LENGTH_LONG).show();
            }
        });
    }
}
