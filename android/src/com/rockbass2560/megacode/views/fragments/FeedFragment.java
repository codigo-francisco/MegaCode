package com.rockbass2560.megacode.views.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.adapters.CustomAdapter;
import com.rockbass2560.megacode.adapters.model.DataModel;
import com.rockbass2560.megacode.viewmodels.FeedViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    private final static String TAG = "FeedFragment";
    private CustomAdapter customAdapter;
    private FeedViewModel feedViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_feed, container, false);

        feedViewModel = ViewModelProviders.of(this).get(FeedViewModel.class);

        feedViewModel.getFeeds().observe(this, dataModels -> {
            customAdapter.setData(dataModels);
            swipeRefreshLayout.setRefreshing(false);
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_feed);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = view.findViewById(R.id.feed_refreshlayout);
        swipeRefreshLayout.setOnRefreshListener(() -> feedViewModel.actualizarFeed(true));

        customAdapter = new CustomAdapter(getActivity());

        recyclerView.setAdapter(customAdapter);

        if (savedInstanceState!=null){
            if (savedInstanceState.getParcelableArrayList("feeds")!=null){
                List<DataModel> dataModels = savedInstanceState.getParcelableArrayList("feeds");
                customAdapter.setData(dataModels);
            }
        }else {
            feedViewModel.actualizarFeed(true);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //outState.putParcelableArrayList("feeds", new ArrayList<>(