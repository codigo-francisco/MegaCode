package com.megacode.screens;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.megacode.adapters.model.DataModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<DataModel> dataModels;

    public CustomAdapter(List<DataModel> dataModels){
        this.dataModels = dataModels;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView titleText;
        TextView subjectText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.circle_image_type);
            titleText = itemView.findViewById(R.id.title_feed);
            subjectText = itemView.findViewById(R.id.subject_feed);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cards_layout, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int listPosition) {
        DataModel dataModel = dataModels.get(listPosition);

        myViewHolder.circleImageView.setImageResource(dataModel.getImagen());
        myViewHolder.titleText.setText(dataModel.getTitle());
        myViewHolder.subjectText.setText(dataModel.getContent());
    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }
}
