package com.megacode.screens;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.megacode.adapters.model.DataModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {


    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_feed, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_feed);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager  linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<DataModel> data = new ArrayList<>();

        //Dummy data
        DataModel dataModel = new DataModel();
        dataModel.setImagen(R.drawable.ico);
        dataModel.setTitle("Titulo");
        dataModel.setContent("Contenido dentro de la carta");

        data.add(dataModel);

        dataModel = new DataModel();
        dataModel.setImagen(R.drawable.megacode);
        dataModel.setTitle("MegaCode");
        dataModel.setContent("Datos de pruebas");

        data.add(dataModel);

        CustomAdapter customAdapter = new CustomAdapter(data);

        recyclerView.setAdapter(customAdapter);

        return view;
    }

}
