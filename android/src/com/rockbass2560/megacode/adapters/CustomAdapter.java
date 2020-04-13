package com.rockbass2560.megacode.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.adapters.model.DataModel;
import com.rockbass2560.megacode.views.activities.ScoreActivity;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<DataModel> dataModels = new ArrayList<>();
    private final Activity activityOwner;

    public CustomAdapter(Activity activityOwner){
        this.activityOwner = activityOwner;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView titleText;
        TextView subjectText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cards_layout_icon);
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

        switch (dataModel.getTypeFeed()){
            case PUNTAJE:
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), ScoreActivity.class);
                        View sharedElement = view.findViewById(R.id.cards_layout_icon);
                        ActivityOptions activityOptions =
                                ActivityOptions.makeSceneTransitionAnimation(activityOwner,sharedElement,"score_icon");
                        activityOwner.startActivity(intent, activityOptions.toBundle());
                    }
                });
                break;
            case JUEGO:
                myViewHolder.itemView.setOnClickListener(dataModel.getClickListener());
                break;
            default:
                break;
        }

        myViewHolder.imageView.setImageResource(dataModel.getImagen());
        myViewHolder.titleText.setText(dataModel.getTitle());
        myViewHolder.subjectText.setText(dataModel.getContent());
    }

    public void setData(List<DataModel> data){
        this.dataModels = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }
}
