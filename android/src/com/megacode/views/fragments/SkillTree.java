package com.megacode.views.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.megacode.R;
import com.megacode.adapters.AdapterRecyclerSkillTree;
import com.megacode.adapters.model.SkillNode;
import com.megacode.models.parcelables.ParcelableLinkedList;
import com.megacode.models.response.NivelesResponse;
import com.megacode.viewmodels.NivelViewModel;
import com.megacode.views.activities.RootActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SkillTree extends Fragment {

    private final static String TAG = SkillTree.class.getName();

    public SkillTree() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skill_tree, container, false);
        NivelViewModel nivelViewModel = ViewModelProviders.of(this).get(NivelViewModel.class);

        FloatingActionButton floatingActionButton =  view.findViewById(R.id.skilltree_play);
        floatingActionButton.setOnClickListener(view1 -> {
            RootActivity rootActivity = (RootActivity)getActivity();
            rootActivity.selectFragment(R.id.jugar);
        });

        RecyclerView recyclerView = view.findViewById(R.id.skill_tree_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AdapterRecyclerSkillTree adapterRecyclerSkillTree = new AdapterRecyclerSkillTree(getFragmentManager());
        recyclerView.setAdapter(adapterRecyclerSkillTree);

        nivelViewModel.getListMutableLiveData().observe(this, new Observer<LinkedList<List<SkillNode>>>() {
            @Override
            public void onChanged(LinkedList<List<SkillNode>> lists) {
                adapterRecyclerSkillTree.setData(lists);
            }
        });

        if (savedInstanceState!=null){
            if (savedInstanceState.getParcelable("nodes")!=null){
                adapterRecyclerSkillTree.setData(((ParcelableLinkedList)savedInstanceState.getParcelable("nodes")).nodes);
            }
        }else{
            nivelViewModel.listarNiveles();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //ParcelableLinkedList parcelableLinkedList = new ParcelableLinkedList(nodes);
        //outState.putParcelable("nodes", parcelableLinkedList);
        super.onSaveInstanceState(outState);
    }

}
