package com.megacode.screens;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.megacode.models.ScoreResponse;

import java.util.List;
import java.util.Locale;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private List<ScoreResponse> scoreModelList;

    public ScoreAdapter(List<ScoreResponse> scoreModelList){
        this.scoreModelList = scoreModelList;
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView nameText;
        TextView scoreText;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.scores_image_profile);
            nameText = itemView.findViewById(R.id.scores_nombre);
            scoreText = itemView.findViewById(R.id.scores_scorenumber);
        }
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_scores, viewGroup, false);

        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder scoreViewHolder, int i) {
        ScoreResponse scoreModel = scoreModelList.get(i);
        scoreViewHolder.scoreText.setText(String.format(Locale.getDefault(),"%d puntos", scoreModel.getScore()));
        scoreViewHolder.nameText.setText(scoreModel.getNombre());
        byte[] bytes = Base64.decode(scoreModel.getFotoPerfil(), Base64.DEFAULT);
        scoreViewHolder.profileImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        //TODO: resaltar los 3 primeros lugares, ubicar la posicion del jugador actual y compararla contra otros jugadores (verificar si está en el top 10)
    }

    @Override
    public int getItemCount() {
        return scoreModelList.size();
    }
}