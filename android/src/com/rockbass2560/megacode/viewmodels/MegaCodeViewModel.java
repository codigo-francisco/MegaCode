package com.rockbass2560.megacode.viewmodels;

import android.app.Application;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rockbass2560.megacode.entities.MegaCode;
import com.rockbass2560.megacode.models.database.Emocion;
import com.rockbass2560.megacode.models.database.Nivel;
import com.rockbass2560.megacode.models.database.NivelTerminado;
import com.rockbass2560.megacode.models.database.Sesion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MegaCodeViewModel extends AndroidViewModel {

    private final static String TAG = MegaCodeViewModel.class.getName();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public MegaCodeViewModel(@NonNull Application application) {
        super(application);
    }

    public void agregarNivelTerminado(NivelTerminado nivelTerminado, Map<String, Integer> puntajes){
        CollectionReference nivelesTerminados = db.collection("Usuarios/"+user.getUid()+"/NivelesTerminados");
        //Buscar si existe el nivelTerminado
        nivelesTerminados.get()
                .addOnSuccessListener(nivelesTerminadosQuery -> {
                    try {
                        List<NivelTerminado> nivelTerminadoList = nivelesTerminadosQuery
                                .getDocuments()
                                .stream()
                                .map(doc -> {
                                    NivelTerminado nivelTerminadoLocal = doc.toObject(NivelTerminado.class);
                                    nivelTerminadoLocal.id = doc.getId();
                                    return nivelTerminadoLocal;
                                })
                                .filter(nt -> nt.nivelId == nivelTerminado.nivelId).collect(Collectors.toList());

                        if (nivelTerminadoList.size() > 0) {
                            NivelTerminado nivelTerminadoRemote = nivelTerminadoList.get(0);
                            if (nivelTerminado.puntaje > nivelTerminadoRemote.puntaje) {
                                nivelTerminadoRemote.terminado = nivelTerminado.terminado;
                                nivelTerminadoRemote.puntaje = nivelTerminado.puntaje;
                                nivelesTerminados.document(nivelTerminadoRemote.id)
                                        .set(nivelTerminadoRemote);
                            }
                        } else {
                            nivelesTerminados.add(nivelTerminado)
                                    .addOnSuccessListener(docReference -> {
                                        actualizarPuntajes(puntajes);
                                    });
                        }
                    }catch (Exception ex){
                        Log.d(TAG, ex.getMessage(), ex);
                    }
                });
    }

    private void actualizarPuntajes(Map<String, Integer> puntajes){
        //Obtenemos los puntajes actuales
        DocumentReference usuariosReference = db.document("/Usuarios/"+user.getUid());

        usuariosReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> documentData = documentSnapshot.getData();

                    if (documentData!=null) {

                        for (Map.Entry<String, Integer> puntaje : puntajes.entrySet()) {
                            long puntajeSumado = puntaje.getValue();

                            puntajeSumado += (long) documentData.getOrDefault(puntaje.getKey(), 0);

                            documentData.put(puntaje.getKey(), puntajeSumado);
                        }

                        usuariosReference.set(documentData);
                    }else{
                        usuariosReference.set(puntajes);
                    }
                });
    }

    public void agregarSesion(Sesion sesionActual) {
        //Conseguir ultima conexión, para agregarsela a la sesion

        CollectionReference sesionesReference = db.collection("Usuarios/"+user.getUid()+"/Sesiones");
        sesionesReference.add(sesionActual);
                /*.addOnSuccessListener(documentReference -> {
                    CollectionReference emocionesCollection = db.collection(documentReference.getPath()+"/emociones");
                    for (Emocion emocion : sesionActual.emociones){
                        emocionesCollection.add(emocion);
                    }
                });*/
    }
}