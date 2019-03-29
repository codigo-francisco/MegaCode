package com.rockbass2560.megacode.adapters;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.rockbass2560.megacode.R;
import com.rockbass2560.megacode.models.database.Score;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private static final String TAG = ScoreAdapter.class.getName();

    private List<Score> scoreModelList = new ArrayList<>();

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_scores, viewGroup, false);

        return new ScoreViewHolder(view);
    }

    private void cargarImagen(Context context, String id, ImageView imageView){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(id + "/fotoPerfil.png");
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCacheControl("max-age=43200")
                .build();
        storageReference.updateMetadata(metadata);
        storageReference.getDownloadUrl()
            .addOnSuccessListener(taskSnapshot -> {
                try {
                    Glide.with(context)
                            .load(taskSnapshot)
                            .placeholder(R.drawable.progress_animation)
                            .into(imageView);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            })
            .addOnFailureListener(failure ->{
                Glide.with(context)
                        .load(R.drawable.ic_baseline_account_box_24px)
                        .into(imageView);
            });
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder scoreViewHolder, int index) {
        Resources resources = scoreViewHolder.itemView.getResources();
        Score scoreModel = scoreModelList.get(index);
        scoreViewHolder.scoreText.setText(String.format(Locale.getDefault(),"%d puntos", scoreModel.score));
        scoreViewHolder.nameText.setText(scoreModel.nombre);

        scoreViewHolder.profileImage.setImageDrawable(scoreViewHolder.itemView.getContext().getDrawable(R.drawable.progress_animation));
        cargarImagen(scoreViewHolder.itemView.getContext(), scoreModel.id, scoreViewHolder.profileImage);

        switch (index){
            case 0: //Primer lugar
                //scoreViewHolder.profileImage.setBackground(resources.getDrawable(R.drawable.first_place));
                scoreViewHolder.nameText.setTextColor(resources.getColor(R.color.md_yellow_700, null));
                scoreViewHolder.scoreText.setTextColor(resources.getColor(R.color.md_yellow_700, null));
                break;
            case 1: //Segundo lugar
                //scoreViewHolder.profileImage.setBackground(resources.getDrawable(R.drawable.second_place));
                scoreViewHolder.nameText.setTextColor(resources.getColor(R.color.md_grey_600, null));
                scoreViewHolder.scoreText.setTextColor(resources.getColor(R.color.md_grey_600, null));
                break;
            case 2: //Tercer lugar
                //scoreViewHolder.profileImage.setBackground(resources.getDrawable(R.drawable.third_place));
                scoreViewHolder.nameText.setTextColor(resources.getColor(R.color.md_brown_700, null));
                scoreViewHolder.scoreText.setTextColor(resources.getColor(R.color.md_brown_700, null));
                break;
            default: //Bug
                scoreViewHolder.changeDefaultValues();
                break;
        }
    }

    public void setData(List<Score> scoreModelList){
        this.scoreModelList = scoreModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (scoreModelList==null) ? 0 : scoreModelList.size();
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
}