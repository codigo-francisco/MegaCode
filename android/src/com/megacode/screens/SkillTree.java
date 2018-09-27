package com.megacode.screens;


import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.megacode.adapters.model.SkillNode;
import com.megacode.models.GeneralCallback;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SkillTree extends Fragment {


    public SkillTree() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skill_tree, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.skill_tree_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO: datos dummy de prueba
        LinkedList<List<SkillNode>> nodes = new LinkedList<>();
        for (int index=0; index < 20; index++){
            List<SkillNode> nivel = new ArrayList<SkillNode>();
            Random random = new Random();
            int numero = random.nextInt(3)+1;
            for (int index2=0; index2< numero; index2++){
                SkillNode skillNode = new SkillNode();
                nivel.add(skillNode);
            }
            nodes.add(nivel);
        }

        AdapterRecyclerSkillTree adapterRecyclerSkillTree = new AdapterRecyclerSkillTree(nodes);
        recyclerView.setAdapter(adapterRecyclerSkillTree);

        return view;
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
