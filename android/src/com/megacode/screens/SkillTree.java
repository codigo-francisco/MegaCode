package com.megacode.screens;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.megacode.adapters.model.SkillNode;
import com.megacode.models.ParcelableLinkedList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class SkillTree extends Fragment {

    private LinkedList<List<SkillNode>> nodes;

    public SkillTree() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skill_tree, container, false);

        FloatingActionButton floatingActionButton =  view.findViewById(R.id.skilltree_play);
        floatingActionButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), MegaCodeAcitivity.class);

            startActivity(intent);
        });

        RecyclerView recyclerView = view.findViewById(R.id.skill_tree_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (savedInstanceState!=null){
            if (savedInstanceState.getParcelable("nodes")!=null){
                nodes = ((ParcelableLinkedList)savedInstanceState.getParcelable("nodes")).nodes;
            }
        }else{
            //TODO: datos dummy de prueba
            nodes = new LinkedList<>();
            for (int index=0; index < 20; index++) {
                List<SkillNode> nivel = new ArrayList<SkillNode>();
                Random random = new Random();
                int numero = random.nextInt(3) + 1;
                for (int index2 = 0; index2 < numero; index2++) {
                    SkillNode skillNode = new SkillNode();
                    nivel.add(skillNode);
                }
                nodes.add(nivel);
            }
        }

        AdapterRecyclerSkillTree adapterRecyclerSkillTree = new AdapterRecyclerSkillTree(nodes);
        recyclerView.setAdapter(adapterRecyclerSkillTree);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        ParcelableLinkedList parcelableLinkedList = new ParcelableLinkedList(nodes);
        outState.putParcelable("nodes", parcelableLinkedList);
        super.onSaveInstanceState(outState);
    }

    class AdapterRecyclerSkillTree extends RecyclerView.Adapter<AdapterRecyclerSkillTree.SkillTreeViewHolder>{

        private LinkedList<List<SkillNode>> nodes;

        public AdapterRecyclerSkillTree(LinkedList<List<SkillNode>> nodes){
            this.nodes = nodes;
        }

        @NonNull
        @Override
        public SkillTreeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View linearLayout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skillrow_layout,viewGroup,false);

            return new SkillTreeViewHolder(linearLayout);
        }

        @Override
        public void onBindViewHolder(@NonNull SkillTreeViewHolder skillTreeViewHolder, int index) {
            LinearLayout linearLayout = (LinearLayout)skillTreeViewHolder.itemView;
            linearLayout.removeAllViews();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());

            List<SkillNode> horizontalNode = nodes.get(index);

            for (SkillNode skillNode : horizontalNode){
                View cardView = layoutInflater.inflate(R.layout.skillnode_layout,linearLayout, false);
                ImageView imageView = cardView.findViewById(R.id.node_imageview);
                imageView.setImageResource(R.drawable.megacode);

                linearLayout.addView(cardView);
            }
        }

        @Override
        public int getItemCount() {
            return nodes.size();
        }

        public class SkillTreeViewHolder extends RecyclerView.ViewHolder{

            public SkillTreeViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

}
