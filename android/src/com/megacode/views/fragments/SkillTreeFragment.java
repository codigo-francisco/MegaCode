package com.megacode.views.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.megacode.Claves;
import com.megacode.R;
import com.megacode.adapters.AdapterRecyclerSkillTree;
import com.megacode.adapters.model.DataModel;
import com.megacode.models.database.NivelConTerminado;
import com.megacode.viewmodels.NivelViewModel;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SkillTreeFragment extends Fragment {

    private final static String TAG = SkillTreeFragment.class.getName();
    private AdapterRecyclerSkillTree adapterRecyclerSkillTree;
    private NivelViewModel nivelViewModel;

    public SkillTreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Claves.ABRIR_NIVEL_MEGACODE) {
            if (resultCode == Activity.RESULT_OK){
                nivelViewModel.listarNiveles();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skill_tree, container, false);
        nivelViewModel = ViewModelProviders.of(this).get(NivelViewModel.class);

        FloatingActionButton floatingActionButton =  view.findViewById(R.id.skilltree_play);
        floatingActionButton.hide();

        nivelViewModel.getSiguienteEjercicio().observe(this, new Observer<DataModel>() {
            @Override
            public void onChanged(DataModel dataModel) {
                floatingActionButton.setOnClickListener(dataModel.getClickListener());
                floatingActionButton.show();
            }
        });


        RecyclerView recyclerView = view.findViewById(R.id.skill_tree_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterRecyclerSkillTree = new AdapterRecyclerSkillTree(getFragmentManager());
        recyclerView.setAdapter(adapterRecyclerSkillTree);

        nivelViewModel.getNiveles().observe(this, new Observer<LinkedList<List<NivelConTerminado>>>() {
            @Override
            public void onChanged(LinkedList<List<NivelConTerminado>> lists) {
                adapterRecyclerSkillTree.setData(lists);
            }
        });

        nivelViewModel.listarNiveles();

        return view;
    }

}
