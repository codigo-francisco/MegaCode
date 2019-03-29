package com.rockbass2560.megacode.views.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

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
    private TextView textView;
    private RecyclerView recyclerView;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_feed, container, false);

        textView = view.findViewById(R.id.feed_text_sincontenido);

        feedViewModel = ViewModelProviders.of(this).get(FeedViewModel.class);

        feedViewModel.observadorUsuario().observe(this, usuario -> {
            feedViewModel.actualizarFeed(true);
        });

        feedViewModel.getFeeds().observe(this, dataModels -> {
            customAdapter.setData(dataModels);
            swipeRefreshLayout.setRefreshing(false);
        });

        recyclerView = view.findViewById(R.id.recycler_view_feed);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = view.findViewById(R.id.feed_refreshlayout);
        swipeRefreshLayout.setOnRefreshListener(() -> feedViewModel.actualizarFeed(true));

        customAdapter = new CustomAdapter(getActivity());
        customAdapter.registerAdapterDataObserver(dataObserver);

        recyclerView.setAdapter(customAdapter);

        feedViewModel.configurarUsuario();

        return view;
    }

    RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkEmpty();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            checkEmpty();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            checkEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            checkEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            checkEmpty();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            checkEmpty();
        }

        public void checkEmpty(){
            if (customAdapter.getItemCount() > 0){
                textView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }else {
                textView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    };
}
