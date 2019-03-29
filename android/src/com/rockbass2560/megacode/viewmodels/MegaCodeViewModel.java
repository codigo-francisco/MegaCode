package com.rockbass2560.megacode.viewmodels;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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

public class MegaCodeViewModel extends AndroidViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public MegaCodeViewModel(@NonNull Application application) {
        super(application);
    }

    public void agregarNivelTerminado(NivelTerminado nivelTerminado){
        CollectionReference nivelesTerminados = db.collection("Usuarios/"+user.getUid()+"/NivelesTerminados");
        //Buscar si existe el nivelTerminado
        nivelesTerminados.get()
                .addOnSuccessListener(nivelesTerminadosQuery -> {
                    List<NivelTerminado> nivelTerminadoList = nivelesTerminadosQuery
                            .getDocuments()
                            .stream()
                            .map(doc -> {
                                NivelTerminado nivelTerminadoLocal = doc.toObject(NivelTerminado.class);
                                nivelTerminadoLocal.id = doc.getId();
                                return nivelTerminadoLocal;
                            })
                            .filter(nt -> nt.nivelId == nivelTerminado.nivelId).collect(Collectors.toList());

                    if (nivelTerminadoList.size() > 0){
                        NivelTerminado nivelTerminadoRemote = nivelTerminadoList.get(0);
                        nivelTerminadoRemote.terminado = nivelTerminado.terminado;
                        nivelTerminadoRemote.puntaje = nivelTerminado.puntaje;
                        nivelesTerminados.document(nivelTerminadoRemote.id)
                                .set(nivelTerminadoRemote);
                    }else{
                        nivelesTerminados.add(nivelTerminado);
                    }
                });
    }

    public void actualizarPuntajes(Map<String, Integer> puntajes){
        //Obtenemos los puntajes actuales
        DocumentReference usuariosReference = db.document("/Usuarios/"+user.getUid());

        usuariosReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> documentData = documentSnapshot.getData();

                    for(Map.Entry<String, Integer> puntaje : puntajes.entrySet()){
                        final long puntajeSumado = puntaje.getValue() + (long)documentData.get(puntaje.getKey());
                        documentData.put(puntaje.getKey(), puntajeSumado);
                    }
                    usuariosReference.set(documentData);
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
