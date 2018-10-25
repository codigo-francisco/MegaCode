package com.megacode.adapters;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.megacode.R;
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

        private void changeDefaultValues(){
            profileImage.setBackground(background);
            nameText.setTextColor(nameTextColor);
            scoreText.setTextColor(scoreTextColor);
        }

        Drawable background;
        int scoreTextColor;
        int nameTextColor;

        private void saveDefaultValues(){
            background = profileImage.getBackground();
            scoreTextColor = scoreText.getCurrentTextColor();
            nameTextColor = nameText.getCurrentTextColor();
        }

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.scores_image_profile);
            nameText = itemView.findViewById(R.id.scores_nombre);
            scoreText = itemView.findViewById(R.id.scores_scorenumber);

            saveDefaultValues();
        }
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_scores, viewGroup, false);

        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder scoreViewHolder, int index) {
        Resources resources = scoreViewHolder.itemView.getResources();
        ScoreResponse scoreModel = scoreModelList.get(index);
        scoreViewHolder.scoreText.setText(String.format(Locale.getDefault(),"%d puntos", scoreModel.getScore()));
        scoreViewHolder.nameText.setText(scoreModel.getNombre());
        byte[] bytes = Base64.decode(scoreModel.getFotoPerfil(), Base64.DEFAULT);
        scoreViewHolder.profileImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

        switch (index){
            case 0: //Primer lugar
                //scoreViewHolder.profileImage.setBackground(resources.getDrawable(R.drawable.first_place));
                scoreViewHolder.nameText.setTextColor(resources.getColor(R.color.md_yellow_700));
                scoreViewHolder.scoreText.setTextColor(resources.getColor(R.color.md_yellow_700));
                break;
            case 1: //Segundo lugar
                //scoreViewHolder.profileImage.setBackground(resources.getDrawable(R.drawable.second_place));
                scoreViewHolder.nameText.setTextColor(resources.getColor(R.color.md_grey_600));
                scoreViewHolder.scoreText.setTextColor(resources.getColor(R.color.md_grey_600));
                break;
            case 2: //Tercer lugar
                //scoreViewHolder.profileImage.setBackground(resources.getDrawable(R.drawable.third_place));
                scoreViewHolder.nameText.setTextColor(resources.getColor(R.color.md_brown_700));
                scoreViewHolder.scoreText.setTextColor(resources.getColor(R.color.md_brown_700));
                break;
            default: //Bug
                scoreViewHolder.changeDefaultValues();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return scoreModelList.size();
    }
}