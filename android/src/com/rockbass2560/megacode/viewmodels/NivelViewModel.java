package com.rockbass2560.megacode.viewmodels;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rockbass2560.megacode.models.database.Nivel;
import com.rockbass2560.megacode.models.database.NivelConTerminado;
import com.rockbass2560.megacode.models.database.NivelTerminado;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NivelViewModel extends AndroidViewModel {

    private MutableLiveData<LinkedList<List<NivelConTerminado>>> nivelesConTerminadoLiveData;

    public NivelViewModel(@NonNull Application application) {
        super(application);

        nivelesConTerminadoLiveData = new MutableLiveData<>();
    }

    public void listarNiveles(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Niveles")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot nivelDocSnap, @Nullable FirebaseFirestoreException e) {
                    if (e == null) {
                        db.collection("Usuarios/" + user.getUid() + "/NivelesTerminados")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot nivelTerminadoDocSnap, @Nullable FirebaseFirestoreException e) {
                                        List<Nivel> niveles = nivelDocSnap
                                                .getDocuments().stream()
                                                .map(docSnap -> {
                                                    Nivel nivel = docSnap.toObject(Nivel.class);
                                                    nivel.id = Integer.parseInt(docSnap.getId());
                                                    return nivel;
                                                })
                                                .sorted()
                                                .collect(Collectors.toList());

                                        List<NivelTerminado> nivelesTerminados = nivelTerminadoDocSnap
                                                .getDocuments().stream()
                                                .map(documentSnapshot -> {
                                                    NivelTerminado nivelTerminado = documentSnapshot.toObject(NivelTerminado.class);
                                                    nivelTerminado.id = documentSnapshot.getId();

                                                    return nivelTerminado;
                                                }).collect(Collectors.toList());

                                        List<NivelConTerminado> nivelesConTerminados = new ArrayList<>();

                                        for (Nivel nivel : niveles) {
                                            NivelConTerminado nivelConTerminado = new NivelConTerminado();
                                            nivelConTerminado.nivel = nivel;
                                            nivelConTerminado.nivelesTerminados = nivelesTerminados.stream().filter(nt -> nt.nivelId == nivel.id).collect(Collectors.toList());

                                            nivelesConTerminados.add(nivelConTerminado);
                                        }

                                        LinkedList<List<NivelConTerminado>> nivelesOrganizados = NivelConTerminado.organizarPorNiveles(nivelesConTerminados);

                                        nivelesConTerminadoLiveData.setValue(nivelesOrganizados);
                                    }
                                });
                    }
                }
            });
    }

    public LiveData<LinkedList<List<NivelConTerminado>>> getNiveles() {
        return nivelesConTerminadoLiveData;
    }
}
