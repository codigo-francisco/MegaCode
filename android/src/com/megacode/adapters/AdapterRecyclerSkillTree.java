package com.megacode.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.megacode.R;
import com.megacode.models.database.Nivel;
import com.megacode.views.fragments.InfoNivel;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRecyclerSkillTree extends RecyclerView.Adapter<AdapterRecyclerSkillTree.SkillTreeViewHolder>{

    private LinkedList<List<Nivel>> nodes = new LinkedList<>();

    private FragmentManager fragmentManager;

    public AdapterRecyclerSkillTree(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
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
        LayoutInflater layoutInflater = LayoutInflater.from(linearLayout.getContext());

        List<Nivel> horizontalNode = nodes.get(index);

        for (Nivel nivel : horizontalNode){
            View cardView = layoutInflater.inflate(R.layout.skillnode_layout,linearLayout, false);
            ImageView imageView = cardView.findViewById(R.id.node_imageview);
            imageView.setImageResource(nivel.getImageResource());
            imageView.setOnClickListener(view->{
                DialogFragment dialogFragment = new InfoNivel();
                Bundle bundle = new Bundle();

                bundle.putParcelable("node", nivel);

                int[] locations= new int[2];
                view.getLocationOnScreen(locations);
                bundle.putInt("sourceX", locations[0]);
                bundle.putInt("sourceY", locations[1]);

                bundle.putInt("heightView", view.getHeight());

                dialogFragment.setArguments(bundle);
                dialogFragment.show(fragmentManager, "dialog_node");
            });

            linearLayout.addView(cardView);
        }
    }

    public void setData(LinkedList<List<Nivel>> nodes){
        this.nodes = nodes;
        notifyDataSetChanged();
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
