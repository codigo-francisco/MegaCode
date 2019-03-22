package com.megacode.views.fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.megacode.R;
import com.megacode.adapters.ScoreAdapter;
import com.megacode.models.response.ScoreResponse;
import com.megacode.viewmodels.ScoreViewModel;

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
        swipeRefreshLayout.setOnRefreshListener(scoreViewModel::obtenerPuntajes);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ScoreAdapter scoreAdapter = new ScoreAdapter();

        recyclerView.setAdapter(scoreAdapter);

        scoreViewModel.getListMutableLiveData().observe(this, new Observer<List<ScoreResponse>>() {
            @Override
            public void onChanged(List<ScoreResponse> scoreResponses) {
                scoreAdapter.setData(scoreResponses);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        scoreViewModel.obtenerPuntajes();

        if (savedInstanceState!=null){
            if (savedInstanceState.getParcelableArrayList("scores")!=null){
                scoreAdapter.setData(savedInstanceState.getParcelableArrayList("scores"));
            }
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //outState.putParcelableArrayList("scores", new ArrayList<>(scoreModelList));
        super.onSaveInstanceState(outState);
    }
}
