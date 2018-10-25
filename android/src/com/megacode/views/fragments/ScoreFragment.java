package com.megacode.views.fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.megacode.R;
import com.megacode.adapters.ScoreAdapter;
import com.megacode.models.ScoreResponse;
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
    private SwipeRefreshLayout swipeRefreshLayout;

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

        if (savedInstanceState!=null){
            if (savedInstanceState.getParcelableArrayList("scores")!=null){
                scoreModelList.addAll(savedInstanceState.getParcelableArrayList("scores"));
                scoreAdapter.notifyDataSetChanged();
            }
        }else{
            createScoreModel();
        }

        swipeRefreshLayout = view.findViewById(R.id.score_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createScoreModel();
            }
        });

        recyclerView.setAdapter(scoreAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("scores", new ArrayList<>(scoreModelList));
        super.onSaveInstanceState(outState);
    }

    private void createScoreModel(){
        scoreModelList.clear();
        MegaCodeServiceInstance.getMegaCodeServiceInstance().megaCodeService.puntajes().enqueue(new Callback<List<ScoreResponse>>() {
            @Override
            public void onResponse(Call<List<ScoreResponse>> call, Response<List<ScoreResponse>> response) {
                if (response.isSuccessful()){
                    scoreModelList.addAll(response.body());

                    scoreAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
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
