package com.rockbass2560.megacode.views.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.adapters.ScoreAdapter;
import com.rockbass2560.megacode.models.database.Score;
import com.rockbass2560.megacode.viewmodels.ScoreViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {

    private final static String TAG = "ScoreFragment";

    public ScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        ScoreViewModel scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.scores_recyclerview);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.score_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(scoreViewModel::actualizarPuntajes);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ScoreAdapter scoreAdapter = new ScoreAdapter();

        recyclerView.setAdapter(scoreAdapter);

        scoreViewModel.observadorPuntajes().observe(this, new Observer<List<Score>>() {
            @Override
            public void onChanged(List<Score> scoreRespons) {
                scoreAdapter.setData(scoreRespons);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        scoreViewModel.actualizarPuntajes();

        return view;
    }
}
