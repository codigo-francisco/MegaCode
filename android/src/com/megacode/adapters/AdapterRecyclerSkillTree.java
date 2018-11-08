package com.megacode.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.megacode.R;
import com.megacode.adapters.model.enumators.TypeLevel;
import com.megacode.models.database.Nivel;
import com.megacode.models.database.NivelConTerminado;
import com.megacode.models.database.NivelTerminado;
import com.megacode.views.fragments.InfoNivel;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRecyclerSkillTree extends RecyclerView.Adapter<AdapterRecyclerSkillTree.SkillTreeViewHolder>{

    private LinkedList<List<NivelConTerminado>> nodes = new LinkedList<>();

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

        List<NivelConTerminado> horizontalNode = nodes.get(index);
        int cantidadNodos = horizontalNode.size();
        int nodosTerminados = 0;
        final boolean bloquear = bloquearNodos;

        for (NivelConTerminado nivelConTerminado : horizontalNode){
            View cardView = layoutInflater.inflate(R.layout.skillnode_layout,linearLayout, false);
            ImageView imageView = cardView.findViewById(R.id.node_imageview);

            Nivel nivel = nivelConTerminado.nivel;
            List<NivelTerminado> nivelesTerminados = nivelConTerminado.nivelesTerminados;

            if (bloquearNodos) {
                //Cambiar el fondo de la vista a gris
                imageView.setBackgroundColor(cardView.getResources().getColor(R.color.md_grey_300));
            }else{
                if (!nivelesTerminados.isEmpty() && nivelesTerminados.get(0).isTerminado()) {
                    nodosTerminados++;
                }
            }

            imageView.setImageResource(nivelConTerminado.getImageResource(bloquearNodos));

            imageView.setOnClickListener(view->{
                DialogFragment dialogFragment = new InfoNivel();
                Bundle bundle = new Bundle();

                bundle.putParcelable("nivel", nivelConTerminado);
                bundle.putBoolean("bloqueado", bloquear);

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

        bloquearNodos = cantidadNodos != nodosTerminados;
    }

    private boolean bloquearNodos = false;

    public void setData(LinkedList<List<NivelConTerminado>> nodes){
        this.nodes = nodes;
        bloquearNodos = false;
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
