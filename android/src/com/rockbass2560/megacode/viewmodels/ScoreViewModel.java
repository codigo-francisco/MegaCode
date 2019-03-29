package com.rockbass2560.megacode.viewmodels;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rockbass2560.megacode.models.Usuario;
import com.rockbass2560.megacode.models.database.Score;

import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ScoreViewModel extends AndroidViewModel {

    private MutableLiveData<List<Score>> scoreResponseLiveData;

    public ScoreViewModel(@NonNull Application application) {
        super(application);

        scoreResponseLiveData = new MutableLiveData<>();
    }

    public void actualizarPuntajes(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Usuarios").addSnapshotListener((queryUsuariosSnapshot, err) ->{
            if ( err == null){
                List<Score> scores = queryUsuariosSnapshot
                        .getDocuments()
                        .stream().map(d -> {
                            Usuario u = d.toObject(Usuario.class);

                            Score score = new Score();
                            score.nombre = u.nombre;
                            score.id = d.getId();
                            score.score = u.getPuntajeTotal();
                            return score;
                        }).collect(Collectors.toList());
                scoreResponseLiveData.setValue(scores);
            }
        });

    }

    public MutableLiveData<List<Score>> observadorPuntajes() {
        return scoreResponseLiveData;
    }

}
