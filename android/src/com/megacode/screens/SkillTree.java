package com.megacode.screens;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.megacode.adapters.model.SkillNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //TODO: datos dummy de prueba
        LinkedList<List<SkillNode>> nodes = new LinkedList<>();
        //1° nivel
        List<SkillNode> primerNivel = new ArrayList<SkillNode>();
        SkillNode skillNode = new SkillNode();
        primerNivel.add(skillNode);
        List<SkillNode> segundoNivel = new ArrayList<>();
        segundoNivel.add(new SkillNode());
        segundoNivel.add(new SkillNode());
        segundoNivel.add(new SkillNode());
        List<SkillNode> tercerNivel = new ArrayList<>();
        tercerNivel.add(new SkillNode());

        nodes.addLast(primerNivel);
        nodes.addLast(segundoNivel);
        nodes.addLast(tercerNivel);

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
        public void onBindViewHolder(@NonNull SkillTreeViewHolder skillTreeViewHolder, int i) {
            //Aquí se crean los circulos
            LinearLayout linearLayout = (LinearLayout)skillTreeViewHolder.itemView;

            List<SkillNode> horizontalNode = nodes.get(i);

            for(SkillNode skillNode : horizontalNode){
                CircleImageView circleImageView = (CircleImageView)LayoutInflater.from(getContext()).inflate(R.layout.skillnode_layout, linearLayout, false);
                circleImageView.setImageResource(R.drawable.megacode);

                linearLayout.addView(circleImageView);
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
